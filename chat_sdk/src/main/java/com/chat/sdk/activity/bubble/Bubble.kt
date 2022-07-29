package com.chat.sdk.activity.bubble

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle
import com.chat.sdk.util.ScreenUtil
import com.chat.sdk.util.ViewUtil

internal class Bubble(context: Context) : FrameLayout(context) {
    companion object {
        fun configureBubble(view: View, chatStyle: ChatStyle, bubbleText:String) {
            if (chatStyle.embedded_window == BubbleType.BAR.type) {
                BarBubble().configureBarBubble(view,chatStyle,bubbleText)
            } else {
                CircularBubble().configureCircularBubble(view, chatStyle)
            }
        }
    }

    init {
        addView(createBubble(context))
    }

    private fun createBubble(context: Context): ConstraintLayout {
        val bubble = ConstraintLayout(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            ScreenUtil().getCircularBubbleWidth(context),
            ScreenUtil().getCircularBubbleWidth(context)
        )
        bubble.layoutParams = layoutParams
        bubble.id = R.id.bubble_layout
        return bubble
    }


}