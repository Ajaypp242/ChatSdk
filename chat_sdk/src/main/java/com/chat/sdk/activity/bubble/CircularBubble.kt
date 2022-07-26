package com.chat.sdk.activity.bubble

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
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

    fun configureBubble(view: View, chatStyle: ChatStyle) {
        val circularView = createCircularLayout(view, chatStyle)
        with(circularView) {
            when (chatStyle.embedded_window) {
                CircularBubbleType.FEMALE.type -> {
                    val imageView = createImageView(view.context)
                    imageView.setImageResource(R.drawable.female)
                    this!!.addView(imageView)
                }
                CircularBubbleType.MALE.type -> {
                    val imageView = createImageView(view.context)
                    imageView.setImageResource(R.drawable.male)
                    this!!.addView(imageView)
                }
                (CircularBubbleType.TEXT_CHAT.type) -> {
                    val textView = createTextView(view.context, "Chat")
                    this!!.addView(textView)
                }
                (CircularBubbleType.TEXT_HELP.type) -> {
                    val textView = createTextView(view.context, "Help")
                    this!!.addView(textView)
                }
                CircularBubbleType.CUSTOMURL.type -> {
                    val imageView = createImageView(view.context)
                    Glide
                        .with(view.context)
                        .load("${BaseUrl.ImageUrl}${chatStyle.custom_chat_bubble}")
                        .centerCrop()
                        .into(imageView)
                    this!!.addView(imageView)
                }
                else -> {
                    val imageView = createIcon(view.context, chatStyle.embedded_window)
                    this!!.addView(imageView)
                }
            }
            setBubbleIconConstraint(this)
            addView(onlineIcon(view.context))
            setOnlineIconConstraint(this)
        }
    }

    private fun createCircularLayout(view: View, chatStyle: ChatStyle): ConstraintLayout? {
        val layout = view.findViewById<ConstraintLayout>(R.id.bubble_layout)
        val unwrappedDrawable =
            AppCompatResources.getDrawable(view.context, R.drawable.circular_bubble_bg)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${chatStyle.chead_color}"))
        }
        view.background = wrappedDrawable
        return layout
    }

    private fun onlineIcon(context: Context): LinearLayout {
        val icon = LinearLayout(context)
        icon.id = R.id.status
        val width = ScreenUtil().getScreenWidth(context)/5
        icon.layoutParams = LinearLayout.LayoutParams(width, width)
        icon.setBackgroundResource(R.drawable.offline_icon)
        return icon
    }

    private fun setOnlineIconConstraint(circularView: ConstraintLayout) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(circularView)
        constraintSet.connect(
            R.id.status,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            R.id.status,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        constraintSet.setMargin(R.id.status, ConstraintSet.TOP, 20)
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

    private fun createImageView(context: Context): CircularImageView {
        val imageView = CircularImageView(context)
        imageView.id = R.id.bubble_icon
        val layoutParams = LinearLayout.LayoutParams(
            ScreenUtil().getScreenWidth(context)-20,
            ScreenUtil().getScreenWidth(context)-20
        )
        imageView.layoutParams = layoutParams
        imageView.borderWidth = 0F
        return imageView
    }

    private fun createTextView(context: Context, text: String): TextView {
        val textView = TextView(context)
        textView.id = R.id.bubble_icon
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.text = text
        textView.textSize = 16F
        textView.setTextColor(Color.WHITE)
        textView.typeface = Typeface.DEFAULT_BOLD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textView.gravity = Gravity.CENTER
        }
        return textView
    }

    private fun createIcon(context: Context, type: String): ImageView {
        val imageView = ImageView(context)
        imageView.id = R.id.bubble_icon
        val layoutParams = LinearLayout.LayoutParams(ScreenUtil().getScreenWidth(context)-50 , ScreenUtil().getScreenWidth(context)-50)
        imageView.layoutParams = layoutParams
        when (type) {
            CircularBubbleType.ICON_7.type -> {
                imageView.setImageResource(R.drawable.seven)
            }
            CircularBubbleType.ICON_10.type -> {
                imageView.setImageResource(R.drawable.ten)
            }
            CircularBubbleType.ICON_12.type -> {
                imageView.setImageResource(R.drawable.twelve)
            }
            else -> {
                imageView.setImageResource(R.drawable.third)
            }
        }
        return imageView
    }
}