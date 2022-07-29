package com.chat.demo

import com.chat.sdk.modal.*

data class Response(
    val chat_style: ChatStyle,
    val chat_form_text: List<ChatFormText>?,
    val proprofs_language_id: String,
)
