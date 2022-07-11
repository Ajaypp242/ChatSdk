package com.chat.sdk.util

import android.content.Context

internal class ScreenUtil {
    fun getScreenWidth(context: Context): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (60 * scale + 0.5f).toInt()
    }
}