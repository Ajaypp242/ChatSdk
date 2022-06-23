package com.chat.sdk.activity.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle
import com.chat.sdk.network.BaseUrl
import com.mikhaellopez.circularimageview.CircularImageView

@SuppressLint("ViewConstructor")
class CircularBubble(context: Context, chatStyle: ChatStyle) : FrameLayout(context) {
    private val bubbleWidth = 300
    private val bubbleHeight = 300
    init {
        val circularView = createCircularLayout(chatStyle)
        when (chatStyle.embedded_window) {
            CircularBubbleType.FEMALE.type -> {
                val imageView = createImageView()
                imageView.setImageResource(R.drawable.female)
                circularView.addView(imageView)
                addView(circularView)
            }
            CircularBubbleType.MALE.type -> {
                val imageView = createImageView()
                imageView.setImageResource(R.drawable.male)
                circularView.addView(imageView)
                addView(circularView)
            }
            (CircularBubbleType.TEXT_CHAT.type) -> {
                val textView = createTextView("Chat")
                circularView.addView(textView)
                addView(circularView)
            }
            (CircularBubbleType.TEXT_HELP.type) -> {
                val textView = createTextView("Help")
                circularView.addView(textView)
                addView(circularView)
            }
            CircularBubbleType.CUSTOMURL.type -> {
                val imageView = createImageView()
                Glide
                    .with(context)
                    .load("${BaseUrl.ImageUrl}${chatStyle.custom_chat_bubble}")
                    .centerCrop()
                    .into(imageView);
                circularView.addView(imageView)
                addView(circularView)
            }
            else -> {
                val imageView = createIcon(chatStyle.embedded_window)
                circularView.addView(imageView)
                addView(circularView)
            }
        }
    }

    private fun createCircularLayout(chatStyle: ChatStyle): LinearLayout {
        val layout = LinearLayout(context)
        layout.layoutParams = LinearLayout.LayoutParams(bubbleWidth, bubbleHeight)
        layout.gravity = Gravity.CENTER
        val unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.circular_bubble_bg)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${chatStyle.chead_color}"))
        }
        layout.background = wrappedDrawable
        return layout
    }

    private fun createImageView(): CircularImageView {
        val imageView = CircularImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.borderWidth = 0F
        return imageView
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.text = text
        textView.textSize = 18F
        textView.setTextColor(Color.WHITE)
        textView.typeface = Typeface.DEFAULT_BOLD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.textAlignment = TEXT_ALIGNMENT_CENTER
            textView.gravity = Gravity.CENTER
        }
        return textView
    }

    private fun createIcon(type:String): ImageView {
        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(bubbleWidth -150, bubbleHeight - 150)
        when (type) {
            CircularBubbleType.ICON_3.type -> {
                imageView.setImageResource(R.drawable.third)
            }
            CircularBubbleType.ICON_7.type -> {
                imageView.setImageResource(R.drawable.seven)
            }
            CircularBubbleType.ICON_10.type -> {
                imageView.setImageResource(R.drawable.ten)
            }
            CircularBubbleType.ICON_12.type -> {
                imageView.setImageResource(R.drawable.twelve)
            }
        }
        return imageView
    }
}