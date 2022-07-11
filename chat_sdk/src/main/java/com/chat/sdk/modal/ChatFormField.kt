package com.chat.sdk.modal

import java.io.Serializable

internal data class ChatFormField(val fleg:String,
                         val fld_name:String,
                         val fld_type:String,
                         val js:String,
                         val jsmsg:String,
                         val sel_item:String,
                         val field_identifier:String,
                         val order:String,
                         val isname:String,
                         val isemail:String,
                         var value:String?,
                         ) : Serializable
