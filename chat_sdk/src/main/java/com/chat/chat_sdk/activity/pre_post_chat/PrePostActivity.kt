package com.chat.chat_sdk.activity.pre_post_chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.chat_sdk.R
import com.chat.chat_sdk.activity.chat.ChatActivity
import com.chat.chat_sdk.databinding.ActivityPrePostBinding
import com.chat.chat_sdk.modal.*
import com.chat.chat_sdk.network.ApiAdapter
import com.chat.chat_sdk.session.Session
import com.chat.chat_sdk.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class PrePostActivity : AppCompatActivity() {
    private val prePostChatSettingData = "prePostChatSettingData"
    private var chatSettingData: ChatSettingData? = null
    private var currentWindowType = ChatWindowType.PRE_CHAT.type
    private var siteId = ""
    private lateinit var viewModel: PrePostViewModal
    var delay = Session.time
    var timer: Timer? = null
    private val adapter = PrePostAdapter()
    private var operatorStatus = ""
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                currentWindowType = ChatWindowType.POST_CHAT.type
                setFormScreen(chatSettingData!!, ChatWindowType.POST_CHAT)
            }
        }
    private lateinit var activityPrePostBinding: ActivityPrePostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPrePostBinding = ActivityPrePostBinding.inflate(layoutInflater)
        setContentView(activityPrePostBinding.root)
        addToolbar()
        chatSettingData = intent.getSerializableExtra(prePostChatSettingData) as ChatSettingData
        siteId = intent.getStringExtra("site_id").toString()
        val layoutManager = LinearLayoutManager(applicationContext)
        activityPrePostBinding.prePostRecylerView.layoutManager = layoutManager
        activityPrePostBinding.prePostRecylerView.adapter = adapter
        activityPrePostBinding.submit.setOnClickListener {
            if (currentWindowType == ChatWindowType.PRE_CHAT.type) {
                val res = Util().validatePrePostFields(activityPrePostBinding.prePostRecylerView)
                if (res.valid) {
                    preSubmitAction(res.param)
                }
            } else if (currentWindowType == ChatWindowType.POST_CHAT.type) {
                val res = Util().validatePrePostFields(activityPrePostBinding.prePostRecylerView)
                if (res.valid) {
                    postSubmitAction(res.param)
                }
            } else if(currentWindowType == ChatWindowType.OFFLINE.type){
                val res = Util().validatePrePostFields(activityPrePostBinding.prePostRecylerView)
                if (res.valid) {
                    offlineSubmitAction(res.param)
                }
            }
        }

        val factory = PrePostViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(PrePostViewModal::class.java)
        viewModel.operators.postValue(chatSettingData?.operator_status)
        viewModel.operators.observe(this) {
            Log.d("PreChatActivity", it.toString())
            Log.d("PreChatActivity", operatorStatus)
            val newStatus = if (it.isEmpty()) "offline" else "online"
            if (operatorStatus != newStatus) {
                operatorStatus = newStatus
                if (operatorStatus == "offline") {
                    currentWindowType = ChatWindowType.OFFLINE.type
                    setFormScreen(chatSettingData!!, ChatWindowType.OFFLINE)
                } else if (operatorStatus == "online") {
                    currentWindowType = ChatWindowType.PRE_CHAT.type
                    setFormScreen(chatSettingData!!, ChatWindowType.PRE_CHAT)
                }
            }
            Log.d("PreChatActivity", operatorStatus)
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        Log.d("PreChatActivity", "onPause")
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewModel.getChats(
                    siteId,
                    chatSettingData!!.proprofs_language_id,
                    chatSettingData!!.proprofs_session,
                )
            }
        }, 0, delay.toLong())
        Log.d("PreChatActivity", "onResume")
    }

    private fun setFormScreen(chatSettingData: ChatSettingData, type: ChatWindowType) {
        val text = Util().getCatTextField(chatSettingData.chat_form_text, type)
        activityPrePostBinding.startTextTitle.text = text.beforesubmit
        activityPrePostBinding.submit.text = text.txt_submit
        val layoutManager = LinearLayoutManager(applicationContext)
        activityPrePostBinding.prePostRecylerView.layoutManager = layoutManager
        val data = Util().getCatTypeFields(chatSettingData.chat_form_field, type)
        val sortedData = Util().sortFieldData(data)
        Log.d("chatFormField", sortedData.toString())
        adapter.setFormFields(sortedData)
    }

    fun startActivity(site_id: String, context: Context, chatSettingData: ChatSettingData) {
        val starter = Intent(context, PrePostActivity::class.java)
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        starter.putExtra(prePostChatSettingData, chatSettingData)
        starter.putExtra("site_id", site_id)
        context.startActivity(starter)
    }

    private fun addToolbar() {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#${chatData.chat_style.chead_color}")))
    }

    private fun postSubmitAction(param: ArrayList<PrePostValidateSubmitType>) {
    }

    private fun preSubmitAction(param: ArrayList<PrePostValidateSubmitType>) {
        CoroutineScope(Dispatchers.IO).launch {
            var preChatResponse: PreChatResponse? = null
            val name = param.find { it -> it.key == "Name" }?.value.toString()
            val email = param.find { it -> it.key == "Email" }?.value.toString()
            try {
                val response = ApiAdapter.apiClient.preChat(
                    chatSettingData?.proprofs_session,
                    chatSettingData?.proprofs_language_id,
                    siteId,
                    "0",
                    "0",
                    "0",
                    email,
                    name,
                    "2",
                    "1657017412",
                    "SlRrSVBHMDUvMDBQdm1uRzYvTUplQT09",
                    "0",
                    "https://www.proprofschat.com/chat-page/?id=MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09",
                    ""
                )

                preChatResponse = response.body()
                if (preChatResponse?.result == 1) {
                    submitPreChatFormData()
                }
            } catch (e: Exception) {
            }
        }
    }


    private fun offlineSubmitAction(param: ArrayList<PrePostValidateSubmitType>) {
        CoroutineScope(Dispatchers.IO).launch {

            val name = param.find { it -> it.key == "Name" }?.value.toString()
            val email = param.find { it -> it.key == "Email" }?.value.toString()
            try {
                val response = ApiAdapter.apiClient.sendOfflineMessage(
                    "0",
                    chatSettingData?.proprofs_session,
                    chatSettingData?.proprofs_language_id,
                    siteId,
                    "0",
                    name,
                    email,
                    "",
                    "3",
                    "1656420636",
                    "SlRrSVBHMDUvMDBQdm1uRzYvTUplQT09",
                    "0",
                    "chat_sdk"
                )
            val res =    response.body()
                Log.d("res",res.toString())

//                preChatResponse = response.body()
//                if (preChatResponse?.result == 1) {
//                    submitPreChatFormData()
//                }
            } catch (e: Exception) {
            }
        }
    }

    private fun submitPreChatFormData() {
        val name = activityPrePostBinding.prePostRecylerView.getChildAt(0)
            .findViewById<EditText>(R.id.value).text.toString()
        val email = activityPrePostBinding.prePostRecylerView.getChildAt(1)
            .findViewById<EditText>(R.id.value).text.toString()
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra(prePostChatSettingData, chatSettingData)
        intent.putExtra("site_id", siteId)
        intent.putExtra("name", name)
        intent.putExtra("email", email)
        getResult.launch(intent)
    }
}
