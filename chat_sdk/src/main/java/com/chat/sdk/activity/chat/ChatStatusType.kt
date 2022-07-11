package com.chat.sdk.activity.chat

internal enum class ChatStatusType(val type:String) {
    CLOSED("0"),
    IDLE("1"),
    ACCEPTED("2"),
    REQUESTED("3"),
}