package com.chat.sdk.activity.form

enum class FormFieldType(val type:String) {
    TEXT("text"),
    TEXTAREA("textarea"),
    RADIO("radio"),
    CHECKBOX("checkbox"),
    DROPDOWN("select"),
    PRIVACY_POLICY("gdpr_chk")
}