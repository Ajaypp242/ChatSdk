package com.chat.sdk.activity.bubble

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle
import com.chat.sdk.network.BaseUrl
import com.chat.sdk.util.ScreenUtil
import com.mikhaellopez.circularimageview.CircularImageView

internal class CircularBubble() {
    fun configureCircularBubble(view: View, chatStyle: ChatStyle) {
        val layout = view.findViewById<ConstraintLayout>(R.id.bubble_layout)
        val circularIconView =
            LayoutInflater.from(view.context).inflate(R.layout.circular_icon, null)
        val circularIconLayout =
            circularIconView.findViewById<LinearLayout>(R.id.circular_bubble_layout)
        circularIconLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val unwrappedDrawable =
            AppCompatResources.getDrawable(view.context, R.drawable.circular_bubble_bg)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${chatStyle.chead_color}"))
        }
        circularIconLayout.background = wrappedDrawable
        configureCircularBubbleType(circularIconLayout, chatStyle)
        layout.addView(circularIconView)
        setBubbleIconConstraint(layout)
        layout.addView(onlineIcon(view))
        setOnlineIconConstraint(layout)
    }

    private fun configureCircularBubbleType(
        circularIconLayout: LinearLayout,
        chatStyle: ChatStyle
    ) {

        val icon = circularIconLayout.findViewById<ImageView>(R.id.icon)
        val text = circularIconLayout.findViewById<TextView>(R.id.text)
        val customIcon = circularIconLayout.findViewById<CircularImageView>(R.id.custom_icon)
        when (chatStyle.embedded_window) {
            CircularBubbleType.CUSTOM_URL.type -> {
                icon.visibility = View.GONE
                text.visibility = View.GONE
                customIcon.visibility = View.VISIBLE
                customIcon.layoutParams.width = ScreenUtil().getCustomImageWidth(customIcon.context)
                customIcon.layoutParams.height = ScreenUtil().getCustomImageWidth(customIcon.context)
                Glide
                    .with(circularIconLayout.context)
                    .load("${BaseUrl.ImageUrl}${chatStyle.custom_chat_bubble}")
                    .centerCrop()
                    .into(customIcon)
            }

            CircularBubbleType.TEXT_HELP.type -> {
                icon.visibility = View.GONE
                customIcon.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.typeface = Typeface.DEFAULT_BOLD
                text.text = text.context.getString(R.string.help)
            }
            CircularBubbleType.TEXT_CHAT.type -> {
                icon.visibility = View.GONE
                customIcon.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.typeface = Typeface.DEFAULT_BOLD
                text.text = text.context.getString(R.string.chat)
            }
            else -> {
                text.visibility = View.GONE
                customIcon.visibility = View.GONE
                icon.visibility = View.VISIBLE
                configureIcon(icon, chatStyle.embedded_window)
            }
        }
    }


    private fun configureIcon(icon: ImageView, type: String) {

        when (type) {
            CircularBubbleType.ICON_7.type -> {
                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.seven)
            }
            CircularBubbleType.ICON_10.type -> {
                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.ten)
            }
            CircularBubbleType.ICON_12.type -> {
                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.twelve)
            }
            CircularBubbleType.ICON_3.type -> {
                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.third)
            }
            CircularBubbleType.MALE.type -> {
                icon.layoutParams.width = ScreenUtil().getImageViewBubbleWidth(icon.context)
                icon.layoutParams.height = ScreenUtil().getImageViewBubbleWidth(icon.context)
                icon.setImageResource(R.drawable.male)
            }
            CircularBubbleType.FEMALE.type -> {
                icon.layoutParams.width = ScreenUtil().getImageViewBubbleWidth(icon.context)
                icon.layoutParams.height = ScreenUtil().getImageViewBubbleWidth(icon.context)
                icon.setImageResource(R.drawable.female)
            }

        }
    }

    private fun onlineIcon(view: View): View? {
        val iconLayout =
            LayoutInflater.from(view.context).inflate(R.layout.online_status_layout, null)
        val width = ScreenUtil().getScreenWidth(view.context) / 5
        iconLayout.layoutParams = LinearLayout.LayoutParams(width, width)
        return iconLayout
    }

    private fun setOnlineIconConstraint(circularView: ConstraintLayout) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(circularView)
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
        constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, 20)
        constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, 20)
        constraintSet.applyTo(circularView)
    }

    private fun setBubbleIconConstraint(circularView: ConstraintLayout) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(circularView)
        constraintSet.connect(
            R.id.bubble_icon,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            R.id.bubble_icon,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        constraintSet.connect(
            R.id.bubble_icon,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            R.id.bubble_icon,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        constraintSet.applyTo(circularView)
    }
}