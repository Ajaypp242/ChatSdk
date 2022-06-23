package com.chat.sdk.modal

import java.io.Serializable

data class ChatFormField(val fleg:String,
                         val fld_name:String,
                         val fld_type:String,
                         val js:String,
                         val jsmsg:String,
                         val sel_item:String,
                         val field_identifier:String,
                         val order:String,
                         val isname:String,
                         val isemail:String) : Serializable


enum class FieldName(val type: String) {
    TEXT("text"),TEXTAREA("textarea"),RADIO("radio")
}