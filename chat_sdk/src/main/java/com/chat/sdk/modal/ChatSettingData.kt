package com.chat.sdk.modal

import java.io.Serializable

internal data class ChatSettingData(
    val chat_style: ChatStyle,
    val chat_form_text: List<ChatFormText>,
    val chat_form_field: List<ChatFormField>,
    val proprofs_language_id: String,
    val proprofs_session: String,
    var operator_status: List<Operator>,
    val chat_status:ChatStatus
) : Serializable
