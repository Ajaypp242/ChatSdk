package com.chat.sdk.util

import android.annotation.SuppressLint
import android.content.SharedPreferences

internal class Session(private val sharedPreferences: SharedPreferences) {

    @SuppressLint("CommitPrefEdits")
    fun setKey(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getKey(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
}