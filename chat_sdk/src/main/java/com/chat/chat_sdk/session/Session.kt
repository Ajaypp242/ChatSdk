package com.chat.chat_sdk.session

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class Session {
    private var sharedPreferences: SharedPreferences? = null
    private val PREFERENCE_NAME = "chat_sdk_preference"

    companion object{
         const val time = 6000
    }



    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun setSessionData(key: String, value: String?) {
        with(sharedPreferences?.edit()) {
            this?.putString(key, value)
            this?.apply()
        }
    }

    fun getSessionData(key: String): String? {
        return sharedPreferences?.getString(key,null)
    }
}