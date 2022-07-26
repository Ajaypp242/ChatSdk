package com.chat.sdk.activity.bubble

import android.view.View
import android.widget.LinearLayout
import com.chat.sdk.R

internal enum class OperatorStatusType(val type:Int) {
    ONLINE(1),
    OFFLINE(0)
}

internal class OperatorStatus()  {
    companion object Status {
        fun changeStatus(bubble:View, status:OperatorStatusType){
            if(status  ==  OperatorStatusType.ONLINE){
                bubble.findViewById<View>(R.id.status) .setBackgroundResource(R.drawable.online_icon)
            } else {
                bubble.findViewById<LinearLayout>(R.id.status).setBackgroundResource(R.drawable.offline_icon)
            }
        }
    }
}