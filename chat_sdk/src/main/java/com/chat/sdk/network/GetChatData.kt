package com.chat.sdk.network

import com.chat.sdk.modal.ChatData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class GetChatData {

    companion object {
        private var LOOP: Boolean = true
    }

    var flow: Flow<ChatData?> = flow {
        while (true) {
            delay(6000)
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

                emit(response.body())
            }
        }

    }.flowOn(Dispatchers.Default)

    fun toggleLoop() {
        LOOP = !LOOP

    }
}