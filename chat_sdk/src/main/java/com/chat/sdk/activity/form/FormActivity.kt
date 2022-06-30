package com.chat.sdk.activity.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.sdk.R
import com.chat.sdk.activity.chat.ChatActivity
import com.chat.sdk.databinding.ActivityFormBinding
import com.chat.sdk.modal.*
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.util.FormUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FormActivity : AppCompatActivity() {
    private val prePostChatSettingData = "prePostChatSettingData"
    private var chatSettingData: ChatSettingData? = null
    private var currentWindowType = ChatWindowType.PRE_CHAT.type
    private var siteId = ""
    private lateinit var viewModel: PrePostViewModal
    private val adapter = FormAdapter()
    private var operatorStatus = ""
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                currentWindowType = ChatWindowType.POST_CHAT.type
                setFormScreen(chatSettingData!!, ChatWindowType.POST_CHAT)
            }
        }
    private lateinit var activityFormBinding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFormBinding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(activityFormBinding.root)
        addToolbar()
        chatSettingData = intent.getSerializableExtra(prePostChatSettingData) as ChatSettingData
        siteId = intent.getStringExtra("site_id").toString()
        val layoutManager = LinearLayoutManager(applicationContext)
        activityFormBinding .formRecyclerView.layoutManager = layoutManager
        activityFormBinding.formRecyclerView.adapter = adapter
        activityFormBinding.submit.setOnClickListener {
            if (currentWindowType == ChatWindowType.PRE_CHAT.type) {
                val res = FormUtil().validateForm(
                    adapter.chatFormField!!,
                    activityFormBinding.formRecyclerView
                )
                if (res.valid) {
                    preSubmitAction()
                }
            } else if (currentWindowType == ChatWindowType.POST_CHAT.type) {
                val res = FormUtil().validateForm(
                    adapter.chatFormField!!,
                    activityFormBinding.formRecyclerView
                )
                if (res.valid) {
                    postSubmitAction(res.param)
                }
            } else if (currentWindowType == ChatWindowType.OFFLINE.type) {
                val res = FormUtil().validateForm(
                    adapter.chatFormField!!,
                    activityFormBinding.formRecyclerView
                )
                if (res.valid) {
                    offlineSubmitAction(res.param)
                }
            }
        }

        val factory = PrePostViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(PrePostViewModal::class.java)
        viewModel.operators.postValue(chatSettingData?.operator_status)
        viewModel.operators.observe(this) {
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
        }
    }

    private fun setFormScreen(chatSettingData: ChatSettingData, type: ChatWindowType) {
        val text = FormUtil().getCatTextField(chatSettingData.chat_form_text, type)
        activityFormBinding.startTextTitle.text = text.beforesubmit
        activityFormBinding.submit.text = text.txt_submit
        val layoutManager = LinearLayoutManager(applicationContext)
        activityFormBinding.formRecyclerView.layoutManager = layoutManager
        val data = FormUtil().getCatTypeFields(chatSettingData.chat_form_field, type)
        val sortedData = FormUtil().sortFieldData(data)
        adapter.setFormFields(sortedData, this)
    }

    fun startActivity(site_id: String, context: Context, chatSettingData: ChatSettingData) {
        val starter = Intent(context, FormActivity::class.java)
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        starter.putExtra(prePostChatSettingData, chatSettingData)
        starter.putExtra("site_id", site_id)
        context.startActivity(starter)
    }

    private fun addToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#${chatData.chat_style.chead_color}")))
    }

    private fun postSubmitAction(param: ArrayList<FormValidationSubmitType>) {
    }

    private fun preSubmitAction() {
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!)
            val preChatResponse: PreChatResponse?
            try {
                val response = ApiAdapter.apiClient.preChat(
                    chatSettingData?.proprofs_session,
                    chatSettingData?.proprofs_language_id,
                    siteId,
                    "0",
                    "0",
                    "0",
                    formSubmitValue.email,
                    formSubmitValue.name,
                    adapter.chatFormField!!.size.toString(),
                    "1657017412",
                    "SlRrSVBHMDUvMDBQdm1uRzYvTUplQT09",
                    "0",
                    "https://www.proprofschat.com/chat-page/?id=MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09",
                    "",
                    formSubmitValue.dynamicStringParams,
                    formSubmitValue.dynamicArrayParams
                )

                preChatResponse = response.body()
                if (preChatResponse?.result == 1) {
                    launchChatActivity(formSubmitValue.name, formSubmitValue.email)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun offlineSubmitAction(param: ArrayList<FormValidationSubmitType>) {
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!)
            try {
                val response = ApiAdapter.apiClient.sendOfflineMessage(
                    "0",
                    chatSettingData?.proprofs_session,
                    chatSettingData?.proprofs_language_id,
                    siteId,
                    "0",
                    formSubmitValue.email,
                    formSubmitValue.name,
                    adapter.chatFormField!!.size.toString(),
                    "1656420636",
                    "SlRrSVBHMDUvMDBQdm1uRzYvTUplQT09",
                    "0",
                    "chat_sdk"
                )
                val res = response.body()
                Log.d("res", res.toString())
            } catch (e: Exception) {
            }
        }
    }

    private fun launchChatActivity(name: String, email: String) {
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra(prePostChatSettingData, chatSettingData)
        intent.putExtra("site_id", siteId)
        intent.putExtra("name", name)
        intent.putExtra("email", email)
        getResult.launch(intent)
    }
}
