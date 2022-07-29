package com.chat.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.chat.sdk.ProProfsChat
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private var liveSiteId: String = "MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ll = findViewById<LinearLayout>(R.id.ll)
        val bubble = ProProfsChat(this, liveSiteId).init()
        ll.addView(bubble)
        testApi()

    }

    fun testApi(){
       val jsonObject = "{'proprofs_language_id':'123'}"

        val gson = Gson()
    val res =    gson.fromJson<Response>(jsonObject,Response::class.java)
        Log.d("response",res.toString())

    }

}