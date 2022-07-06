package com.chat.sdk.modal

import java.io.Serializable

data class ChatStatus(
    val status:String,
    val returning_visitor:String,
    val rating:String
): Serializable
