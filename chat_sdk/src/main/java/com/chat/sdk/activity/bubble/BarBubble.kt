package com.chat.sdk.activity.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.DrawableCompat
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle

internal class BarBubble {
    @SuppressLint("InflateParams")
    fun configureBarBubble(view: View, chatStyle: ChatStyle, bubbleText: String) {
        val bubbleView = view.findViewById<ConstraintLayout>(R.id.bubble_layout)
        val layoutParams = bubbleView.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        val barBubble = LayoutInflater.from(view.context).inflate(R.layout.bubble_bar, null)
        val barBubbleLayout = barBubble.findViewById<LinearLayout>(R.id.bar_bubble_layout)
        val bubbleTextView = barBubbleLayout.findViewById<TextView>(R.id.bubble_text)
        bubbleTextView.text = bubbleText
        val unwrappedDrawable =
            AppCompatResources.getDrawable(view.context, R.drawable.bar_bubble_bg)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${chatStyle.chead_color}"))
        }
        barBubbleLayout.background = wrappedDrawable
        bubbleView.addView(barBubble)
        setBarConstraint(bubbleView)
        bubbleView.addView(onlineIcon(view.context))
        setOnlineIconConstraint(bubbleView)
    }

    @SuppressLint("InflateParams")
    private fun onlineIcon(context: Context): View? {
        val iconLayout = LayoutInflater.from(context).inflate(R.layout.online_status_layout, null)
//        val width = ScreenUtil().getScreenWidth(context) / 4
        iconLayout.layoutParams = LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return iconLayout
    }

    private fun setOnlineIconConstraint(barView: ConstraintLayout) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(barView)
        constraintSet.connect(
            R.id.status_layout,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            R.id.status_layout,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, 30)
        constraintSet.applyTo(barView)
    }

    private fun setBarConstraint(barView: ConstraintLayout) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(barView)
        constraintSet.connect(
            R.id.bar_bubble_layout,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            R.id.bar_bubble_layout,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        constraintSet.connect(
            R.id.bar_bubble_layout,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            R.id.bar_bubble_layout,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        constraintSet.setMargin(R.id.bar_bubble_layout, ConstraintSet.TOP, 10)
        constraintSet.applyTo(barView)
    }
}