package com.chat.sdk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import com.chat.sdk.activity.bubble.*
import com.chat.sdk.activity.chat.ChatActivity
import com.chat.sdk.activity.chat.ChatStatusType
import com.chat.sdk.activity.form.FormActivity
import com.chat.sdk.modal.*
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.network.GetChatData
import com.chat.sdk.util.CommonUtil
import com.chat.sdk.util.Constant
import com.chat.sdk.util.Session
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ProProfsChat(private val context: Context, private val site_id: String) :
    ProProfsChatInterface {
    private lateinit var bubble: View
    private var chatSettingData: ChatSettingData? = null
    private var operatorStatus: OperatorStatusType = OperatorStatusType.NONE
    private lateinit var sharedPreferences: SharedPreferences
    private var chatStatus: String? = null

    internal companion object {
        internal var messages: ArrayList<Message>? = null
        var operatorName = ""
        var operatorPhoto = ""
        var account_id = "0"

        fun resetMessagesAndOperatorDetails() {
            messages = null
            operatorName = ""
            operatorPhoto = ""
        }
    }

    override fun init(): View {
        sharedPreferences =
            context.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val sessionId = Session(sharedPreferences).getKey(Constant.SESSION_KEY)
        bubble = Bubble(context)
        CoroutineScope(Dispatchers.Main).launch {
            getData(sessionId, sharedPreferences)
        }
        CommonUtil().getTimeZone()
        CommonUtil().getTimeZone2()
        return bubble
    }

    private suspend fun getData(
        session_id: String?,
        sharedPreferences: SharedPreferences
    ) {
        try {
            val response = ApiAdapter.apiClient.getData(
                site_id, "", session_id, "sdk_${site_id}",
                "", "", "", "", "",
                "", "", "","0"
            )
            chatSettingData = response.body()
            if (chatSettingData?._ProProfs_SDK_Status == null) {
                chatStatus = chatSettingData!!.chat_status.status
                Session(sharedPreferences).setKey(
                    Constant.SESSION_KEY,
                    chatSettingData!!.proprofs_session
                )
                account_id = chatSettingData!!.ProProfs_accounts
                Bubble.configureBubble(
                    bubble,
                    chatSettingData!!.chat_style,
                    chatSettingData?.chat_header_text!!.chat_online_text
                )
                getChatData()
            }
        } catch (e: Exception) {
        }
    }

    private fun getChatData() {
        if (chatSettingData != null) {
            ChatData.site_id = site_id
            ChatData.ProProfs_Session = chatSettingData?.proprofs_session
            ChatData.proprofs_language_id = chatSettingData?.proprofs_language_id
            GetChatData.chatDataSharedFlow = GetChatData().getSharedFlow()

            CoroutineScope(Dispatchers.IO).launch {
                GetChatData.chatDataSharedFlow.collect { value ->
                    chatStatus = value.chat_status
                    chatSettingData!!.operator_status = value.operator_status
                    updateOperatorStatus(value.operator_status)
                }
            }
            bubble.setOnClickListener {
                navigationOnChatStatus()
            }
        }
    }

    private fun updateOperatorStatus(operators: List<Operator>) {
        val newStatus =
            if (operators.isEmpty()) OperatorStatusType.OFFLINE else OperatorStatusType.ONLINE
        if (operatorStatus != newStatus) {
            operatorStatus = newStatus
            OperatorStatus.changeStatus(bubble, operatorStatus)
        }
    }

    private fun navigationOnChatStatus() {
        ChatData.ProProfs_Msg_Counter = "0"
        if (chatStatus != null) {
            when (chatStatus) {
                ChatStatusType.ACCEPTED.type -> {
                    launchChatActivity()
                }
                ChatStatusType.REQUESTED.type -> {
                    launchChatActivity()
                }
                else -> {
                    launchFormActivity()
                }
            }
        }
    }

    private fun launchChatActivity() {
        val name = Session(sharedPreferences).getKey(Constant.VISITOR_NAME)
        val email = Session(sharedPreferences).getKey(Constant.VISITOR_EMAIL)
        ChatData.ProProfs_Visitor_name = name!!
        ChatData.ProProfs_Visitor_email = email!!
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("chatSettingData", chatSettingData)
        intent.putExtra("site_id", site_id)
        intent.putExtra("name", name)
        intent.putExtra("email", email)
        context.startActivity(intent)
    }

    private fun launchFormActivity() {
        val formType: FormType =
            if (chatSettingData!!.operator_status.isEmpty()) FormType.OFFLINE else FormType.PRE_CHAT
        val starter = Intent(context, FormActivity::class.java)
        starter.putExtra("chatSettingData", chatSettingData)
        starter.putExtra("site_id", site_id)
        starter.putExtra("form_type", formType)
        context.startActivity(starter)
    }
}