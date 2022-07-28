package com.chat.sdk.modal

import java.io.Serializable

data class StaticLanguage(
    val placeholder_text: String,
    val closing_text: String,
    val end_chat_text: String,
    val continue_text: String,
    val cancel_text: String,
    val branding_text: String,
    val support_text: String
):Serializable
