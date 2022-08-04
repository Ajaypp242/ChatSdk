package com.chat.sdk.activity.bubble

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
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

internal class CircularBubble {
    @SuppressLint("InflateParams")
    fun configureCircularBubble(view: View, chatStyle: ChatStyle) {
        val layout = view.findViewById<ConstraintLayout>(R.id.bubble_layout)
        val layoutParams = layout.layoutParams
//        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
//        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        val circularIconView =
            LayoutInflater.from(view.context).inflate(R.layout.circular_icon, null)
        val circularIconLayout =
            circularIconView.findViewById<LinearLayout>(R.id.circular_bubble_layout)
//        circularIconLayout.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )

        configureCircularBubbleType(circularIconLayout, chatStyle)
        layout.addView(circularIconView)
        setBubbleIconConstraint(layout)
        layout.addView(onlineIcon(view))
        setOnlineIconConstraint(layout,chatStyle)
    }

    private fun configureCircularBubbleType(
        circularIconLayout: LinearLayout,
        chatStyle: ChatStyle
    ) {
        val icon = circularIconLayout.findViewById<ImageView>(R.id.icon)
        val text = circularIconLayout.findViewById<TextView>(R.id.text)
        val customIcon = circularIconLayout.findViewById<ImageView>(R.id.custom_icon)
        val staticImage = circularIconLayout.findViewById<ImageView>(R.id.static_image)
        when (chatStyle.embedded_window) {
            CircularBubbleType.CUSTOM_URL.type -> {
                icon.visibility = View.GONE
                text.visibility = View.GONE
                staticImage.visibility = View.GONE
                customIcon.visibility = View.VISIBLE
                Glide
                    .with(circularIconLayout.context)
                    .load("${BaseUrl.ImageUrl}${chatStyle.custom_chat_bubble}")
                    .circleCrop()
                    .into(customIcon)
            }
            CircularBubbleType.MALE.type -> {
                icon.visibility = View.GONE
                text.visibility = View.GONE
                customIcon.visibility = View.GONE
                staticImage.visibility = View.VISIBLE
                staticImage.setImageResource(R.drawable.male)
            }
            CircularBubbleType.FEMALE.type -> {
                icon.visibility = View.GONE
                text.visibility = View.GONE
                customIcon.visibility = View.GONE
                staticImage.visibility = View.VISIBLE
                staticImage.setImageResource(R.drawable.female)
            }

            CircularBubbleType.TEXT_HELP.type -> {
                addBackgroundInBubble(circularIconLayout,chatStyle)
                icon.visibility = View.GONE
                customIcon.visibility = View.GONE
                staticImage.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.typeface = Typeface.DEFAULT_BOLD
                text.text = text.context.getString(R.string.help)
            }
            CircularBubbleType.TEXT_CHAT.type -> {
                addBackgroundInBubble(circularIconLayout,chatStyle)
                icon.visibility = View.GONE
                customIcon.visibility = View.GONE
                staticImage.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.typeface = Typeface.DEFAULT_BOLD
                text.text = text.context.getString(R.string.chat)
            }
            else -> {
                addBackgroundInBubble(circularIconLayout, chatStyle)
                text.visibility = View.GONE
                customIcon.visibility = View.GONE
                staticImage.visibility = View.GONE
                icon.visibility = View.VISIBLE
                configureIcon(icon, chatStyle)
            }
        }
    }


    private fun addBackgroundInBubble(
        circularIconLayout: LinearLayout,
        chatStyle: ChatStyle
    ) {
        val unwrappedDrawable =
            AppCompatResources.getDrawable(
                circularIconLayout.context,
                R.drawable.circular_bubble_bg
            )
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${chatStyle.chead_color}"))
        }
        circularIconLayout.background = wrappedDrawable
    }

    private fun configureIcon(
        icon: ImageView,
        chatStyle: ChatStyle
    ) {
        when (chatStyle.embedded_window) {
            CircularBubbleType.ICON_7.type -> {
//                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
//                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.seven)
            }
            CircularBubbleType.ICON_10.type -> {
//                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
//                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.ten)
            }
            CircularBubbleType.ICON_12.type -> {
//                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
//                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.twelve)
            }
            CircularBubbleType.ICON_3.type -> {
//                icon.layoutParams.width = ScreenUtil().getCircularIconWidth(icon.context)
//                icon.layoutParams.height = ScreenUtil().getCircularIconWidth(icon.context)
                icon.setImageResource(R.drawable.third)
            }


        }
    }

    @SuppressLint("InflateParams")
    private fun onlineIcon(view: View): View? {
        val iconLayout =
            LayoutInflater.from(view.context).inflate(R.layout.online_status_layout, null)
//        iconLayout.layoutParams = LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return iconLayout
    }

    private fun setOnlineIconConstraint(circularView: ConstraintLayout,chatStyle: ChatStyle) {
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
        when (chatStyle.embedded_window) {
            CircularBubbleType.MALE.type->{
                constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin4)))
                constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin4)))
            }
            CircularBubbleType.FEMALE.type->{
                constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin4)))
                constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin4)))
            }
            CircularBubbleType.CUSTOM_URL.type ->{
                constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin1)))
                constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin1)))
            }
            else ->{
            constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin3)))
            constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin3)))
            }
        }

//        constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin10)))
//        constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, ScreenUtil().getPixelValue(circularView.context,circularView.context.resources.getDimension(R.dimen.margin10)))
        if(chatStyle.embedded_window == CircularBubbleType.CUSTOM_URL.type){
//            constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, 10)
//            constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, 10)
        } else {
//            constraintSet.setMargin(R.id.status_layout, ConstraintSet.TOP, 20)
//            constraintSet.setMargin(R.id.status_layout, ConstraintSet.RIGHT, 20)
        }

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