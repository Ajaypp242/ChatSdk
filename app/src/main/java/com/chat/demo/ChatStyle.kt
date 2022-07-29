package com.chat.demo

import java.io.Serializable


 data class ChatStyle(
    val embedded_window:String,
    val chead_color:String,
    val cwin_size:String,
    val chat_window_position:String,
    val dept_enable:String,
    val opchat_enable:String,
    val chat_visitor_name_color:String,
    val chat_operator_name_color:String,
    val rate_chat:String,
    val addchtm_time:String,
    val logo_img:String,
    val pchatfrm:String,
    val pchatfrmurl:String,
    val cmailfrm:String,
    val cmailfrmurl:String,
    val waittime:String,
    val custom_chat_bubble:String,
    val no_offimg:String
    ) : Serializable{

}
