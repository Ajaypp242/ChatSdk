package com.chat.sdk.network

import com.chat.sdk.modal.ChatData
import com.chat.sdk.util.Constant
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class GetChatData {

    companion object {
        private var LOOP: Boolean = true
        var chatDataSharedFlow: MutableSharedFlow<ChatData> = MutableSharedFlow()
    }

    fun toggleLoop() {
        LOOP = !LOOP
    }

    fun getSharedFlow(): MutableSharedFlow<ChatData> {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (LOOP) {
                    val response = ApiAdapter.apiClient.getChat(
                        ChatData.site_id,
                        ChatData.proprofs_language_id,
                        ChatData.ProProfs_Session,
                        ChatData.ProProfs_Msg_Counter,
                        ChatData.ProProfs_Visitor_TimeZone,
                        ChatData.ProProfs_invitation_type,
                        ChatData.ProProfs_Current_URL,
                        ChatData.ProProfsGroupIdHardCoded,
                        ChatData.ProProfs_Visitor_name,
                        ChatData.ProProfs_Visitor_email,
                        ChatData.ProProfs_typing_message
                    )
                    chatDataSharedFlow.emit(response.body()!!)
                }
                delay(Constant.CHAT_API_DELAY)
            }
        }

        return chatDataSharedFlow
    }

    fun resetChatData() {
        ChatData.ProProfs_Msg_Counter = "0"
        ChatData.ProProfs_Visitor_name = ""
        ChatData.ProProfs_Visitor_email = ""
        ChatData.ProProfs_typing_message = ""
    }
}