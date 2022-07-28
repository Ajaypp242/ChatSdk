package com.chat.sdk.modal

import java.io.Serializable

data class ChatHeaderText(val chat_offline_text:String,
                          val offlinemsg:String,
                          val aftermail:String,
                          val beforechat:String,
                          val chat_online_text:String
                          ): Serializable
