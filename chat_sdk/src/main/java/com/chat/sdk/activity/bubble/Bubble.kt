package com.chat.sdk.activity.bubble

import android.content.Context
import android.view.View
import com.chat.sdk.modal.ChatStyle

class Bubble()  {

     fun createBubble(context: Context, chatStyle: ChatStyle): View {
         return if(chatStyle.embedded_window == "1"){
             BarBubble(context,chatStyle)
         } else{
             CircularBubble(context,chatStyle)
         }
    }
}