package com.chat.sdk.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log

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

    fun getCustomImageWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels/7
    }




    fun isDarkModeEnable(context: Context): Boolean {

        val nightModeFlags: Int =context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

       if(nightModeFlags == Configuration.UI_MODE_NIGHT_YES){
           return true
       }
        return false

        Log.d("nightModeFlags",nightModeFlags.toString())
//        Log.d("nightModeFlags",Configuration.UI_MODE_NIGHT_MASK.toString())

        Log.d("nightModeFlagsYes",Configuration.UI_MODE_NIGHT_YES.toString())
        Log.d("nightModeFlagsNo",Configuration.UI_MODE_NIGHT_NO.toString())

    }
}