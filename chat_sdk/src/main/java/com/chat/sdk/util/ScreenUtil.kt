package com.chat.sdk.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

internal class ScreenUtil {
    fun getScreenWidth(context: Context): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (60 * scale + 0.5f).toInt()
    }

    fun getBarBubbleWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
       return (2 * displayMetrics.widthPixels)/3
    }
}