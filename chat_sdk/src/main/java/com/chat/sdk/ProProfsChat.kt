package com.chat.sdk

import android.content.Context
import android.view.View
import com.chat.sdk.activity.bubble.*
import com.chat.sdk.activity.form.FormActivity
import com.chat.sdk.modal.ChatData
import com.chat.sdk.modal.ChatSettingData
import com.chat.sdk.modal.Operator
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.network.GetChatData
import com.chat.sdk.session.Session
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ProProfsChat {
    private var bubble: View? = null
    private var chatSettingData: ChatSettingData? = null
    lateinit var context: Context
    private var operatorStatus: OperatorStatusType = OperatorStatusType.OFFLINE
    fun init(context: Context, site_id: String): View? {
        ChatData.site_id = site_id
        if (bubble != null) {
            return bubble
        }
        this.context = context
        Session().init(context)
        bubble = Bubble(context)
        CoroutineScope(Dispatchers.Main).launch {
            getData(site_id)
        }
        return bubble
    }

    private suspend fun getData(site_id: String) {
        try {
            val response = ApiAdapter.apiClient.getData(
                site_id, "", "", "",
                "", "", "", "", "",
                "", "", ""
            )
            chatSettingData = response.body()
            CircularBubble().configureBubble(bubble!!, chatSettingData!!.chat_style)
            configureSetting()
        } catch (e: Exception) {
        }
    }

    private fun configureSetting() {
        if (chatSettingData != null) {
            updateOperatorStatus(chatSettingData!!.operator_status)
            ChatData.ProProfs_Session = chatSettingData?.proprofs_session
            ChatData.proprofs_language_id = chatSettingData?.proprofs_language_id
            bubble!!.setOnClickListener {
                FormActivity().startActivity(ChatData.site_id!!, context, chatSettingData!!)
            }
            updateOperatorStatus(chatSettingData!!.operator_status)
            CoroutineScope(Dispatchers.IO).launch {
                GetChatData().flow.collect { value ->
                    updateOperatorStatus(value!!.operator_status)
                }
            }
        }
    }

    private fun updateOperatorStatus(operators: List<Operator>) {
        val newStatus =
            if (operators.isEmpty()) OperatorStatusType.OFFLINE else OperatorStatusType.ONLINE
        if (operatorStatus != newStatus) {
            operatorStatus = newStatus
            OperatorStatus.changeStatus(bubble!!, operatorStatus)
        }
    }
}