package com.chat.demo

import android.content.Intent
import com.chat.chat_sdk.Bubble
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.chat.chat_sdk.activity.chat.ChatActivity
import com.chat.demo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var live_site_id: String = "MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09"
//    val live_site_id: String = "47875"
//        var dev_site_id:String = "100634"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val bubble = Bubble().init(this, live_site_id)
        if (bubble != null) {
            binding.chatBubbleContainer.addView(bubble)
        }
        binding.btn.setOnClickListener {
            val starter = Intent(applicationContext, MainActivity2::class.java)
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext .startActivity(starter)
        }
    }
}