package com.chat.sdk.activity.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chat.sdk.ProProfsChat
import com.chat.sdk.R
import com.chat.sdk.activity.form.FormActivity
import com.chat.sdk.databinding.ActivityChatBinding
import com.chat.sdk.modal.ChatData
import com.chat.sdk.modal.ChatSettingData
import com.chat.sdk.modal.FormType
import com.chat.sdk.modal.Message
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.network.BaseUrl
import com.chat.sdk.network.GetChatData
import com.chat.sdk.util.CommonUtil
import com.chat.sdk.util.FileUtils
import com.chat.sdk.util.FormUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class ChatActivity : AppCompatActivity() {
    private var chatSettingData: ChatSettingData? = null
    //private var visitorName: String? = null
    //private var visitorEmail: String? = null
    private var siteId: String = ""
    private lateinit var activityChatBinding: ActivityChatBinding
    private lateinit var viewModel: ChatViewModal
    private lateinit var adapter: ChatAdapter
    private var rating = 0
    private lateinit var dialog: AlertDialog
    private var lastMessageId = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        super.onCreate(savedInstanceState)
        activityChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(activityChatBinding.root)
        getIntentData()
        addToolbar()
        setHeader()
        setLayoutManager()
        initViewModal()
        setFooter()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ProProfsChat.messages = adapter.messages
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    private fun initViewModal() {
        val factory = ChatViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ChatViewModal::class.java)
        viewModel.chatData.observe(this) {
            chatSettingData!!.operator_status = it.operator_status
            if (it.chat_status == ChatStatusType.CLOSED.type) {
                goToPreviousActivity(0)
            } else {
                if (it.messages.isNotEmpty()) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    if (ChatData.ProProfs_Msg_Counter == "0") {
                        adapter.setChatList(it.messages)
                    } else {
                        adapter.addChatList(it.messages)
                    }
                    ChatData.ProProfs_Msg_Counter = it.messages[it.messages.size - 1].sno
                    activityChatBinding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    if (lastMessageId != it.messages[it.messages.size - 1].sno) {
                        updateMessageStatus()
                        lastMessageId = it.messages[it.messages.size - 1].sno
                    }
                }
                if (it.operator_details is List<*>) {
                    val operatorDetails = it.operator_details[0]
                    val operatorDetailsObject = Gson().toJsonTree(operatorDetails).asJsonObject
                    updateOperatorInfo(
                        operatorDetailsObject.get("name").asString,
                        operatorDetailsObject.get("photourl").asString
                    )
                }
            }
        }
        viewModel.visitorMessage.observe(this) {
            if (adapter.itemCount == 0) {
                adapter.setChatList(it)
            } else {
                adapter.addChatList(it)
            }
            activityChatBinding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun getIntentData() {
        chatSettingData = intent.getSerializableExtra("chatSettingData") as ChatSettingData
        //visitorName = intent.getStringExtra("name")
        //visitorEmail = intent.getStringExtra("email")
        siteId = intent.getStringExtra("site_id").toString()
    }

    private fun setHeader() {
        if (chatSettingData!!.chat_style.rate_chat == "Y") {
            activityChatBinding.ratingLayout.visibility = VISIBLE
            ratingUISetup()
        } else {
            activityChatBinding.ratingLayout.visibility = GONE
        }
    }

    private fun setLayoutManager() {
        val layoutManager = LinearLayoutManager(applicationContext)
        activityChatBinding.chatRecyclerView.layoutManager = layoutManager
        adapter = ChatAdapter(chatSettingData!!.chat_style, applicationContext)
        activityChatBinding.chatRecyclerView.adapter = adapter
        if (ProProfsChat.messages != null) {
            adapter.setChatList(ProProfsChat.messages!!)
            activityChatBinding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            updateOperatorInfo(ProProfsChat.operatorName, ProProfsChat.operatorPhoto)
        }
        dialog = CommonUtil().customLoadingDialogAlert(
            this,
            layoutInflater,
            "Please Wait...",
            chatSettingData!!.chat_style.chead_color
        )
        if (adapter.itemCount == 0) {
            dialog.show()
        }
    }

    private fun setFooter() {
        val button = activityChatBinding.sendBtn
        val unwrappedDrawable =
            AppCompatResources.getDrawable(applicationContext, R.drawable.send_btn_background)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(
                wrappedDrawable,
                Color.parseColor("#${chatSettingData!!.chat_style.chead_color}")
            )
        }
        button.background = wrappedDrawable
        button.setOnClickListener {
            sendMessage(activityChatBinding.messageBox.text.toString())
            activityChatBinding.messageBox.setText("")
        }
        onFileChooseResult()
    }


    private fun ratingUISetup() {
        val stars = arrayOf(
            activityChatBinding.star1,
            activityChatBinding.star2,
            activityChatBinding.star3,
            activityChatBinding.star4,
            activityChatBinding.star5
        )
        activityChatBinding.star1.setOnClickListener {
            FormUtil().starRating(
                1,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star
            )
            rating = 1
            rateToOperator()
        }
        activityChatBinding.star2.setOnClickListener {
            FormUtil().starRating(
                2,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star
            )
            rating = 2
            rateToOperator()
        }
        activityChatBinding.star3.setOnClickListener {
            FormUtil().starRating(
                3,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star
            )
            rating = 3
            rateToOperator()
        }
        activityChatBinding.star4.setOnClickListener {
            FormUtil().starRating(
                4,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star
            )
            rating = 4
            rateToOperator()
        }
        activityChatBinding.star5.setOnClickListener {
            FormUtil().starRating(
                5,
                applicationContext,
                chatSettingData!!.chat_style.chead_color,
                stars,
                R.drawable.star
            )
            rating = 5
            rateToOperator()
        }
    }

    private fun onFileChooseResult() {
        val imageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (it.data != null) {
                        val uri = it.data?.data
                        val base64 = FileUtils().uriToBase64(uri!!, applicationContext)
                        val fileExtension = FileUtils().getFileExtension(uri,applicationContext)
                        Log.d("fileExtension",fileExtension.toString())
//                        val bitmap  = FileUtils().base64ToBitmap("")
//                        activityChatBinding.operatorImage.setImageBitmap(bitmap)
                        if (base64 != null && fileExtension != null) {
                            uploadImage(base64,fileExtension)
                        }
                    }
                }
            }

        activityChatBinding.attachment.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            imageResult.launch(intent)
        }
    }

    private fun addToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#${chatSettingData!!.chat_style.chead_color}")))
        val closeBtn = findViewById<ImageView>(R.id.close_icon)
        closeBtn.visibility = VISIBLE
        closeBtn.setOnClickListener {
            closeChatAlert()
        }

        val minimize = findViewById<ImageView>(R.id.minimize_icon)
        minimize.setOnClickListener {
            ProProfsChat.messages = adapter.messages
            finish()
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    private fun sendMessage(message: String) {
        if (message.trim() != "") {
            val visitorMessage =
                Message("", message, "", CommonUtil().getCurrentTime(), "", "", "null", "v")
            Log.d("visitorMessage",visitorMessage.toString())
            val messageList = ArrayList<Message>()
            messageList.add(visitorMessage)
            viewModel.addMessage(messageList)
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    ApiAdapter.apiClient.sendVisitorMessage(
                        chatSettingData?.proprofs_session, message
                    )
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun closeChatAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to close this session?")
        builder.setPositiveButton("Continue") { dialog, which ->
        }
        builder.setNegativeButton("End Chat ") { dialog, which ->
            closeChat()
        }
        builder.show()
    }

    private fun closeChat() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiAdapter.apiClient.generateTranscript(
                    chatSettingData?.proprofs_session, siteId, "2"
                )
                val transcriptId = response.body()
                if (transcriptId != null) {
                    GetChatData().resetChatData()
                    goToPreviousActivity(transcriptId.toInt())
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun goToPreviousActivity(transcriptId: Int) {
        ProProfsChat.resetMessagesAndOperatorDetails()
        val intent = Intent(applicationContext, FormActivity::class.java)
        val postFormIsEmpty = FormUtil().getCatTypeFields(
            chatSettingData!!.chat_form_field,
            FormType.POST_CHAT
        ).isEmpty()
        var formType: FormType = FormType.POST_CHAT
        if (postFormIsEmpty && chatSettingData!!.chat_style.rate_chat == "") {
            formType = FormType.PRE_CHAT
        }
        intent.putExtra("chatSettingData", chatSettingData)
        intent.putExtra("site_id", siteId)
        intent.putExtra("transcriptId", transcriptId)
        intent.putExtra("rating", rating)
        intent.putExtra("form_type", formType)
        startActivity(intent)
        finish()
    }

    private fun updateOperatorInfo(name: String, photo: String) {
        if (activityChatBinding.operatorName.text != name) {
            ProProfsChat.operatorName = name
            ProProfsChat.operatorPhoto = photo
            activityChatBinding.operatorName.text = name
            Glide
                .with(applicationContext)
                .load("${BaseUrl.OperatorImageBaseUrl}${photo}")
                .centerCrop()
                .into(activityChatBinding.operatorImage)
        }
    }

    private fun rateToOperator() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiAdapter.apiClient.submitRating(
                    siteId,
                    chatSettingData!!.proprofs_session,
                    rating
                )
            } catch (e: Exception) {
            }
        }
    }

    private fun updateMessageStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiAdapter.apiClient.updateMessageStatus(
                    chatSettingData!!.proprofs_session
                )
            } catch (e: Exception) {
            }
        }
    }

    private fun uploadImage(file: String,fileExtension:String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
               dialog.show()
                val response = ApiAdapter.apiClient.uploadImage(
                    "5",
                    chatSettingData!!.proprofs_session,
                    siteId,
                    "sdk",
                   "data:image/$fileExtension;base64,$file"
                ).body()
                if(response?.error == "0"){
                    val visitorMessage =
                        Message("", response.file_name, "", CommonUtil().getCurrentTime(), "", "i", "null", "v")
                    Log.d("visitorMessage",visitorMessage.toString())
                    val messageList = ArrayList<Message>()
                    messageList.add(visitorMessage)
                    viewModel.addMessage(messageList)
                }
               dialog.dismiss()
            } catch (e: Exception) {
                dialog.dismiss()
            }
        }
    }
}