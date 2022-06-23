package com.chat.sdk.modal

import java.io.Serializable

data class ChatSettingData(
    val chat_style: ChatStyle,
    val chat_form_text: List<ChatFormText>,
    val chat_form_field: List<ChatFormField>,
    val proprofs_language_id: String,
    val proprofs_session: String,
    val operator_status: List<Operator>
) : Serializable
