package com.chat.sdk.activity.bubble

import android.content.Context
import android.os.Build
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.chat.sdk.R
import com.chat.sdk.util.Constant
import com.chat.sdk.util.ScreenUtil

class Bubble(context: Context) : FrameLayout(context) {
    init {
        addView(createBubble(context))
    }

    private fun createBubble(context: Context): ConstraintLayout {
        val bubble = ConstraintLayout(context)
        bubble.layoutParams = ConstraintLayout.LayoutParams(ScreenUtil().getScreenWidth(context), ScreenUtil().getScreenWidth(context))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bubble.elevation = 15F
        }
        bubble.id = R.id.bubble_layout
        return bubble
    }
}