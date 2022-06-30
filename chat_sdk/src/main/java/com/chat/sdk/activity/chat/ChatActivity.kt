package com.chat.sdk.activity.chat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.sdk.R
import com.chat.sdk.databinding.ActivityChatBinding
import com.chat.sdk.modal.ChatSettingData
import com.chat.sdk.modal.Message
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.session.Session
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private val prePostChatSettingData = "prePostChatSettingData"
    private var chatSettingData: ChatSettingData? = null
    private var delay = Session.time
    private var timer: Timer? = null
    private var visitorName: String? = null
    private var visitorEmail: String? = null
    private var siteId: String? = null
    private lateinit var activityChatBinding: ActivityChatBinding
    private var messageCounter = "0"
    private lateinit var viewModel: ChatViewModal
    private val adapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(activityChatBinding.root)
        addToolbar()
        chatSettingData = intent.getSerializableExtra(prePostChatSettingData) as ChatSettingData
        visitorName = intent.getStringExtra("name")
        visitorEmail = intent.getStringExtra("email")
        siteId = intent.getStringExtra("site_id").toString()

        val layoutManager = LinearLayoutManager(applicationContext)
        activityChatBinding.chatRecyclerView.layoutManager = layoutManager
        activityChatBinding.chatRecyclerView.adapter = adapter
        activityChatBinding.sendBtn.setOnClickListener {
            sendMessage(activityChatBinding.messageBox.text.toString())
            activityChatBinding.messageBox.setText("")
        }
       val closeBtn = findViewById<ImageView>(R.id.close_icon)
        closeBtn.setOnClickListener {
            closeChatAlert()
        }

        val factory = ChatViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ChatViewModal::class.java)
    }


    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewModel.getChats(
                    siteId!!,
                    chatSettingData!!.proprofs_language_id,
                    chatSettingData!!.proprofs_session,
                    messageCounter,
                    visitorName!!,
                    visitorEmail!!
                )
            }
        }, 0, delay.toLong())
        viewModel.messages.observe(this) {
            if (it.isNotEmpty()) {
                if (messageCounter == "0") {
                    adapter.setChatList(it)
                } else {
                    adapter.addChatList(it)
                }
                messageCounter = it[it.size - 1].sno
                activityChatBinding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        viewModel.visitorMessage.observe(this) {
            if (adapter.itemCount == 0) {
                adapter.setChatList(it)
            } else {
                adapter.addChatList(it)
            }
            activityChatBinding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    private fun addToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#${chatData.chat_style.chead_color}")))
    }

    private fun sendMessage(message: String) {
        if (message.trim() != "") {
            val visitorMessage = Message("", message, "", "", "", "", "null", "v")
            val messageList = ArrayList<Message>()
            messageList.add(visitorMessage)
            viewModel.addMessage(messageList)
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = ApiAdapter.apiClient.sendVisitorMessage(
                        chatSettingData?.proprofs_session, message
                    )
                    val chatData = response.body()
                    if (chatData != null) {
                        Log.d("chat_response", chatData.toString())
                    }
                } catch (e: Exception) {
                    Log.d("chat_response", e.toString())
                }
            }
        }
    }

   private fun closeChatAlert(){
       val builder = AlertDialog.Builder(this)
       builder.setMessage("Are you sure you want to close this session?")
       builder.setPositiveButton("Continue") { dialog, which ->

       }
       builder.setNegativeButton("End Chat ") { dialog, which ->
           closeChat()
       }
       builder.show()
   }

    private fun closeChat(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiAdapter.apiClient.generateTranscript(
                    chatSettingData?.proprofs_session, siteId,"2"
                )
                Log.d("chat_close",response.body().toString())
                    if(response.body() != null){
                        timer?.cancel()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
            } catch (e: Exception) {
                Log.d("chat_close", e.toString())
            }
        }
    }
}