package com.chat.sdk.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

internal class ScreenUtil {
        fun getScreenWidth(context: Context): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (60 * scale + 0.5f).toInt()
    }

    fun getBarBubbleWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
       return (2 * displayMetrics.widthPixels)/3
    }

    fun getBarBubbleHeight(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.heightPixels/11
    }

    fun getCircularBubbleWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels/6
    }

    fun getCircularIconWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels/7
    }

    fun getImageViewBubbleWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels/5
    }

    fun getCustomImageWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels/7
    }


    fun getMarginInDP(context: Context, size: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics

        ) as Int
    }

    fun getPixelValue(context: Context, dimenId: Float): Int {
        val resources: Resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dimenId.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}