package com.chat.sdk.activity.form

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.sdk.ProProfsChat
import com.chat.sdk.R
import com.chat.sdk.activity.bubble.OperatorStatusType
import com.chat.sdk.activity.chat.ChatActivity
import com.chat.sdk.databinding.ActivityFormBinding
import com.chat.sdk.modal.*
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.util.CommonUtil
import com.chat.sdk.util.Constant
import com.chat.sdk.util.FormUtil
import com.chat.sdk.util.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


internal class FormActivity : AppCompatActivity() {
    private var chatSettingData: ChatSettingData? = null
    private var currentFormType: FormType? = null
    private var siteId = ""
    private lateinit var viewModel: PrePostViewModal
    private var adapter = FormAdapter()
    private var operatorStatus = OperatorStatusType.OFFLINE
    private var rating = 0
    private lateinit var activityFormBinding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out)
        super.onCreate(savedInstanceState)
        activityFormBinding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(activityFormBinding.root)
        ProProfsChat.resetMessagesAndOperatorDetails()
        getIntentData()
        ratingUISetup()
        addToolbar()
        setLayoutManager()
        initViewModal()
        footer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
    }

    private fun footer(){
        if(chatSettingData?.site_setting?.branding == "1"){
            activityFormBinding.branding.layoutResource = R.layout.footer
            activityFormBinding.branding.inflate()
        }
    }

    private fun initViewModal() {
        val factory = PrePostViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(PrePostViewModal::class.java)
        operatorStatus = getOperatorStatus(chatSettingData!!.operator_status)
        setFormScreen(chatSettingData!!, currentFormType!!)
        viewModel.operators.observe(this) { changeFormType(it) }
    }

    private fun setLayoutManager() {
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
                when (currentFormType) {
                    FormType.PRE_CHAT -> {
                        preSubmitAction()
                    }
                    FormType.POST_CHAT -> {
                        val transcriptId = intent.getIntExtra("transcriptId",0)
                        postSubmitAction(transcriptId)
                    }
                    FormType.OFFLINE -> {
                        offlineSubmitAction()
                    }
                    else -> {
                        offlineSubmitAction()
                    }
                }
            }
        }
    }

    private fun getIntentData() {
        chatSettingData = intent.getSerializableExtra("chatSettingData") as ChatSettingData
        siteId = intent.getStringExtra("site_id").toString()
        rating = intent.getIntExtra("rating", 0)
        currentFormType = intent.getSerializableExtra("form_type") as FormType
    }

    private fun ratingUISetup() {
        val stars = arrayOf(
            activityFormBinding.star1,
            activityFormBinding.star2,
            activityFormBinding.star3,
            activityFormBinding.star4,
            activityFormBinding.star5
        )
        FormUtil().starRating(
            rating,
            applicationContext,
            chatSettingData!!.chat_style.chead_color,
            stars,
            R.drawable.star_big
        )
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
        setLayoutManager()
        setToolbarText()
        val text = FormUtil().getCatTextField(chatSettingData.chat_form_text, type)
        activityFormBinding.formTextMessage.text = text.beforesubmit
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#${chatSettingData!!.chat_style.chead_color}")
        }
        findViewById<ImageView>(R.id.minimize_icon).setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
        }
        setToolbarText()
    }

    private fun setToolbarText(){
        val toolbarTitle = findViewById<TextView>(R.id.header_title)
        if(currentFormType == FormType.OFFLINE){
            toolbarTitle.text = chatSettingData?.chat_header_text?.chat_offline_text
        } else {
            toolbarTitle.text = chatSettingData?.chat_header_text?.chat_online_text
        }
    }

    private fun postSubmitAction(transcript_id: Int) {
        val alertDialog = CommonUtil().customLoadingDialogAlert(
            this,
            layoutInflater,
            "",
            chatSettingData!!.chat_style.chead_color
        )
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!,"post_pp_fld_")
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
                    ""
                )
            } catch (e: Exception) {
            }
            finally {
                alertDialog.dismiss()
                finish()
            }
        }
    }

    private fun preSubmitAction() {
        val alertDialog = CommonUtil().customLoadingDialogAlert(
            this,
            layoutInflater,
            "",
            chatSettingData!!.chat_style.chead_color
        )
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!,"pp_fld_")
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
                    "",
                    "",
                    "0",
                    "https://www.proprofschat.com/chat-page/?id=${chatSettingData!!.proprofs_session}",
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
        val alertDialog = CommonUtil().customLoadingDialogAlert(
            this,
            layoutInflater,
            "",
            chatSettingData!!.chat_style.chead_color
        )
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
        val messageView: TextView = alertDialog.findViewById(android.R.id.message)!!
        messageView.gravity = Gravity.CENTER
        CoroutineScope(Dispatchers.IO).launch {
            val formSubmitValue = FormUtil().getFormValues(adapter.chatFormField!!,"off_pp_fld_")
            try {
                ApiAdapter.apiClient.sendOfflineMessage(
                    "0",
                    chatSettingData?.proprofs_session,
                    chatSettingData?.proprofs_language_id,
                    siteId,
                    "0",
                    formSubmitValue.name,
                    formSubmitValue.email,
                    formSubmitValue.dynamicStringParams,
                    formSubmitValue.dynamicArrayParams,
                    adapter.chatFormField!!.size.toString(),
                    "",
                    "",
                    "0",
                    "chat_sdk"
                )
                alertDialog.dismiss()
                CoroutineScope(Dispatchers.Main).launch {
                    alertAfterOfflineSubmit()
                }

            } catch (e: Exception) {

            }
        }
    }

    private fun launchChatActivity(name: String, email: String) {
        ChatData.ProProfs_Visitor_name = name
        ChatData.ProProfs_Visitor_email = email

        val sharedPreferences = applicationContext.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        Session(sharedPreferences).setKey(Constant.VISITOR_NAME, name)
        Session(sharedPreferences).setKey(Constant.VISITOR_EMAIL, email)
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra("chatSettingData", chatSettingData)
        intent.putExtra("site_id", siteId)
//        intent.putExtra("name", name)
//        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    private fun alertAfterOfflineSubmit() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(chatSettingData?.chat_header_text?.aftermail)
        builder.setNegativeButton("Ok ") { _, _ ->
            finish()
        }
        builder.show()
    }
}
