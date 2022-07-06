package com.chat.sdk.activity.form

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.sdk.R
import com.chat.sdk.activity.bubble.OperatorStatusType
import com.chat.sdk.activity.chat.ChatActivity
import com.chat.sdk.databinding.ActivityFormBinding
import com.chat.sdk.modal.*
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.util.CommonUtil
import com.chat.sdk.util.FormUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FormActivity : AppCompatActivity() {
    private var chatSettingData: ChatSettingData? = null
    private var currentFormType: FormType? = null
    private var siteId = ""
    private lateinit var viewModel: PrePostViewModal
    private val adapter = FormAdapter()
    private var operatorStatus = OperatorStatusType.OFFLINE
    private var rating = 0

    //    private val getResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == RESULT_OK) {
//                currentFormType = FormType.POST_CHAT
//                setFormScreen(chatSettingData!!, FormType.POST_CHAT)
//            }
//        }
    private lateinit var activityFormBinding: ActivityFormBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFormBinding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(activityFormBinding.root)
        val stars = arrayOf(
            activityFormBinding.star1,
            activityFormBinding.star2,
            activityFormBinding.star3,
            activityFormBinding.star4,
            activityFormBinding.star5
        )
        chatSettingData = intent.getSerializableExtra("chatSettingData") as ChatSettingData
        siteId = intent.getStringExtra("site_id").toString()
        rating = intent.getIntExtra("rating", 0)
        FormUtil().starRating(
            rating,
            applicationContext,
            chatSettingData!!.chat_style.chead_color,
            stars,
            R.drawable.star_big
        )
        currentFormType = intent.getSerializableExtra("form_type") as FormType
        addToolbar()
        val layoutManager = LinearLayoutManager(applicationContext)
        activityFormBinding.formRecyclerView.layoutManager = layoutManager
        activityFormBinding.formRecyclerView.adapter = adapter

        if (currentFormType == FormType.POST_CHAT && chatSettingData!!.chat_style.rate_chat == "Y") {
            activityFormBinding.ratingLayout.visibility = View.VISIBLE
        } else {
            activityFormBinding.ratingLayout.visibility = View.GONE
        }

        val button = activityFormBinding.submit
        button.setBackgroundColor(Color.parseColor("#${chatSettingData!!.chat_style.chead_color}"))
        button.setOnClickListener {
            val res = FormUtil().validateForm(
                adapter.chatFormField!!,
                activityFormBinding.formRecyclerView
            )
            if (res.valid) {
                if (currentFormType == FormType.PRE_CHAT) {
                    preSubmitAction()
                } else if (currentFormType == FormType.POST_CHAT) {
                    val transcriptId = intent.getStringExtra("transcriptId")
                    if (transcriptId != null)
                        postSubmitAction(transcriptId)
                } else if (currentFormType == FormType.OFFLINE) {
                    offlineSubmitAction()
                }
            }
        }

        val factory = PrePostViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(PrePostViewModal::class.java)
        operatorStatus = getOperatorStatus(chatSettingData!!.operator_status)
        setFormScreen(chatSettingData!!, currentFormType!!)
        viewModel.operators.observe(this) { changeFormType(it) }

        activityFormBinding.star1.setOnClickListener {
            FormUtil().starRating(
                1,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star_big
            )
            rating = 1
        }
        activityFormBinding.star2.setOnClickListener {
            FormUtil().starRating(
                2,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star_big
            )
            rating = 2
        }
        activityFormBinding.star3.setOnClickListener {
            FormUtil().starRating(
                3,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star_big
            )
            rating = 3
        }
        activityFormBinding.star4.setOnClickListener {
            FormUtil().starRating(
                4,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star_big
            )
            rating = 4
        }
        activityFormBinding.star5.setOnClickListener {
            FormUtil().starRating(
                5,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star_big
            )
            rating = 5
        }
    }

    private fun changeFormType(operators: List<Operator>) {
        if (currentFormType != FormType.POST_CHAT) {
            val newStatus = getOperatorStatus(operators)
            if (operatorStatus != newStatus) {
                operatorStatus = newStatus
                if (operatorStatus == OperatorStatusType.OFFLINE) {
                    currentFormType = FormType.OFFLINE
                    setFormScreen(chatSettingData!!, FormType.OFFLINE)
                } else if (operatorStatus == OperatorStatusType.ONLINE) {
                    currentFormType = FormType.PRE_CHAT
                    setFormScreen(chatSettingData!!, FormType.PRE_CHAT)
                }
            }
        }
    }

    private fun getOperatorStatus(operators: List<Operator>): OperatorStatusType {
        return if (operators.isEmpty())
            OperatorStatusType.OFFLINE
        else OperatorStatusType.ONLINE
    }

    private fun setFormScreen(chatSettingData: ChatSettingData, type: FormType) {
        val text = FormUtil().getCatTextField(chatSettingData.chat_form_text, type)
        activityFormBinding.startTextTitle.text = text.beforesubmit
        activityFormBinding.submit.text = text.txt_submit
        val layoutManager = LinearLayoutManager(applicationContext)
        activityFormBinding.formRecyclerView.layoutManager = layoutManager
        val data = FormUtil().getCatTypeFields(chatSettingData.chat_form_field, type)
        val sortedData = FormUtil().sortFieldData(data)
        adapter.setFormFields(sortedData, this)
    }


    private fun addToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#${chatSettingData!!.chat_style.chead_color}")))
    }

    private fun postSubmitAction(transcript_id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!)
            try {
                ApiAdapter.apiClient.postChat(
                    transcript_id,
                    rating,
                    chatSettingData!!.proprofs_session,
                    chatSettingData!!.proprofs_language_id,
                    siteId,
                    formSubmitValue.dynamicStringParams,
                    formSubmitValue.dynamicArrayParams,
                    adapter.chatFormField!!.size.toString(),
                    "SlRrSVBHMDUvMDBQdm1uRzYvTUplQT09"
                )
                Log.d("formParams", formSubmitValue.dynamicStringParams.toString())
                Log.d("formParams", formSubmitValue.dynamicStringParams.toString())
            } catch (e: Exception) {
            }
        }
    }

    private fun preSubmitAction() {
        val alertDialog = CommonUtil().showProcessSpinner(this, "Please wait...")
        alertDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!)
            val preChatResponse: PreChatResponse?
            try {
                val response = ApiAdapter.apiClient.preChat(
                    chatSettingData!!.proprofs_session,
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
                alertDialog.dismiss()
                if (preChatResponse?.result == 1) {
                    launchChatActivity(formSubmitValue.name, formSubmitValue.email)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun offlineSubmitAction() {
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
            } catch (e: Exception) {
            }
        }
    }

    private fun launchChatActivity(name: String, email: String) {
        ChatData.ProProfs_Visitor_name = name
        ChatData.ProProfs_Visitor_email = email
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra("chatSettingData", chatSettingData)
        intent.putExtra("site_id", siteId)
        intent.putExtra("name", name)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }
}
