package com.chat.sdk.activity.bubble

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.chat.sdk.R

class OnlineStatus()  {
    companion object Status {
        fun changeStatus(bubble:View, value:Int){
            if(value ==  1){
                bubble.findViewById<LinearLayout>(R.id.status).setBackgroundResource(R.drawable.online_icon)
            } else {
                bubble.findViewById<LinearLayout>(R.id.status).setBackgroundResource(R.drawable.offline_icon)
            }

        }
    }
}