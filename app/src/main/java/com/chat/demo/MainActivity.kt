package com.chat.demo

import com.chat.sdk.ProProfsChat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chat.demo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var live_site_id: String = "MXd4bDEwYzFRbW5oNVpBaDI4WUQ1QT09"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val bubble = ProProfsChat().init(this, live_site_id)
        if (bubble != null) {
            binding.ll.addView(bubble)
        }
    }
}