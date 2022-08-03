package com.chat.sdk.activity.bubble

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chat.sdk.R
import com.chat.sdk.util.ScreenUtil

internal enum class OperatorStatusType(val type:Int) {
    ONLINE(1),
    OFFLINE(0)
}

internal class OperatorStatus()  {
    companion object Status {
        fun changeStatus(bubble:View, status:OperatorStatusType){
            val activity = bubble.context as Activity
            activity.runOnUiThread(Runnable {
                if(status  ==  OperatorStatusType.ONLINE){
                bubble.findViewById<TextView>(R.id.operator_status) .setBackgroundResource(R.drawable.online_icon)
                } else {
                bubble.findViewById<TextView>(R.id.operator_status).setBackgroundResource(R.drawable.offline_icon)
                }
            })

        }
    }
}