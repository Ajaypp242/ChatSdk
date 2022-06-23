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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
    private val bubbleIconId = 2
    init {
        val circularView = createCircularLayout(chatStyle)

        when (chatStyle.embedded_window) {
            CircularBubbleType.FEMALE.type -> {
                val imageView = createImageView()
                imageView.setImageResource(R.drawable.female)
                circularView.addView(imageView)
            }
            CircularBubbleType.MALE.type -> {
                val imageView = createImageView()
                imageView.setImageResource(R.drawable.male)
                circularView.addView(imageView)
            }
            (CircularBubbleType.TEXT_CHAT.type) -> {
                val textView = createTextView("Chat")
                circularView.addView(textView)
            }
            (CircularBubbleType.TEXT_HELP.type) -> {
                val textView = createTextView("Help")
                circularView.addView(textView)
            }
            CircularBubbleType.CUSTOMURL.type -> {
                val imageView = createImageView()
                Glide
                    .with(context)
                    .load("${BaseUrl.ImageUrl}${chatStyle.custom_chat_bubble}")
                    .centerCrop()
                    .into(imageView)
                circularView.addView(imageView)
            }
            else -> {
                val imageView = createIcon(chatStyle.embedded_window)
                circularView.addView(imageView)
                setBubbleIconConstraint(circularView)
            }
        }
        circularView.addView(onlineIcon())
        setOnlineIconConstraint(circularView)
        addView(circularView)
    }

    private fun createCircularLayout(chatStyle: ChatStyle): ConstraintLayout {
        val layout = ConstraintLayout(context)
        layout.layoutParams = ConstraintLayout.LayoutParams(bubbleWidth, bubbleHeight)
        val unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.circular_bubble_bg)
        val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#${chatStyle.chead_color}"))
        }
        layout.background = wrappedDrawable
        return layout
    }

    private  fun onlineIcon(): LinearLayout {
     val  icon = LinearLayout(context)
        icon.id = R.id.status
        icon.layoutParams =  LinearLayout.LayoutParams(60, 60)
        return icon
    }

    private fun setOnlineIconConstraint(circularView: ConstraintLayout){
        val constraintSet = ConstraintSet()
        constraintSet.clone(circularView)
        constraintSet.connect(R.id.status,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP)
        constraintSet.connect(R.id.status,ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT)
        constraintSet.setMargin(R.id.status,ConstraintSet.RIGHT,15)
        constraintSet.applyTo(circularView)
    }

    private fun setBubbleIconConstraint(circularView: ConstraintLayout){
        val constraintSet = ConstraintSet()
        constraintSet.clone(circularView)
        constraintSet.connect(bubbleIconId,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP)
        constraintSet.connect(bubbleIconId,ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT)
        constraintSet.connect(bubbleIconId,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM)
        constraintSet.connect(bubbleIconId,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT)
        constraintSet.applyTo(circularView)
    }

    private fun createImageView(): CircularImageView {
        val imageView = CircularImageView(context)
        imageView.id = bubbleIconId
        imageView.layoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.borderWidth = 0F
        return imageView
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(context)
        textView.id = bubbleIconId
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
        imageView.id = bubbleIconId
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