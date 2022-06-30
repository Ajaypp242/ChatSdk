package com.chat.sdk.activity.bubble

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle
import com.chat.sdk.network.BaseUrl
import com.chat.sdk.util.Constant
import com.mikhaellopez.circularimageview.CircularImageView

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