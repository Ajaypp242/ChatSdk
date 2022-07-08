package com.chat.demo

import android.graphics.Color
import com.chat.sdk.ProProfsChat
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.chat.demo.databinding.ActivityMainBinding
import com.chat.sdk.activity.bubble.BarBubble


class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
    private var live_site_id: String = "MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            val ll = findViewById<LinearLayout>(R.id.ll)
//        val cl = findViewById<ConstraintLayout>(R.id.cl)
//        ll.addView()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
////        ProProfsChat.messages
        val bubble = ProProfsChat(this,live_site_id).init()
        if (bubble != null) {
            ll.addView(bubble)
        }
    }
}