package com.chat.sdk.util

import android.content.Context

internal class ScreenUtil {
    fun getScreenWidth(context: Context): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (60 * scale + 0.5f).toInt()
    }

    fun getBarBubbleWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
       return (2 * displayMetrics.widthPixels)/3
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
}