package com.chat.sdk.activity.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.DrawableCompat
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle

@SuppressLint("ViewConstructor")
internal class BarBubble(context: Context, chatStyle: ChatStyle) : FrameLayout(context) {
    init {
      addView(createBarLayout(chatStyle))
    }

    @SuppressLint("InflateParams")
    private fun createBarLayout(chatStyle: ChatStyle): CardView {
        val layout = CardView(context)
        layout.layoutParams = LinearLayout.LayoutParams(1000, 250)
        val unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.bar_bubble_bg)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${ chatStyle.chead_color}"))
        }
        layout.background = wrappedDrawable
        val view = LayoutInflater.from(context).inflate(R.layout.bubble_bar,null)
        layout.addView(view)
        return layout
    }
}