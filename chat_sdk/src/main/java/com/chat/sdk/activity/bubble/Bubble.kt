package com.chat.sdk.activity.bubble

import android.content.Context
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.chat.sdk.R
import com.chat.sdk.util.Constant

class Bubble(context: Context) : FrameLayout(context) {
    init {
        addView(createBubble(context))
    }

    private fun createBubble(context: Context): ConstraintLayout {
        val layout = ConstraintLayout(context)
        layout.layoutParams = ConstraintLayout.LayoutParams(Constant.BUBBLE_WIDTH, Constant.BUBBLE_HEIGHT)
        layout.id = R.id.bubble_layout
        return layout
    }
}