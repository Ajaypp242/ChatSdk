package com.chat.sdk.util

import android.content.Context
import android.util.DisplayMetrics

class ScreenUtil {
    fun getScreenWidth(context: Context): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels
        return  dpWidth
    }

    fun getScreenHeight(context: Context): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels
        return  dpHeight
    }
}