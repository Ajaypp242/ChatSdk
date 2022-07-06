package com.chat.demo

import android.graphics.Color
import com.chat.sdk.ProProfsChat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chat.demo.databinding.ActivityMainBinding
import com.chat.sdk.activity.bubble.BarBubble


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var live_site_id: String = "MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        ProProfsChat.messages
        val bubble = ProProfsChat(this,live_site_id).init()
        if (bubble != null) {
            binding.ll.addView(bubble)
        }
    }
}