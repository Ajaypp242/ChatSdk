package com.chat.sdk.activity.bubble

import android.content.Context
import android.os.Build
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.chat.sdk.R
import com.chat.sdk.util.ScreenUtil

internal class Bubble(context: Context) : FrameLayout(context) {
    init {
        addView(createBubble(context))
    }

    private fun createBubble(context: Context): ConstraintLayout {
        val bubble = ConstraintLayout(context)
       val layoutParams = ConstraintLayout.LayoutParams(ScreenUtil().getScreenWidth(context), ScreenUtil().getScreenWidth(context))
        bubble.layoutParams = layoutParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bubble.elevation = 15F
        }
        bubble.id = R.id.bubble_layout
        return bubble
    }
}