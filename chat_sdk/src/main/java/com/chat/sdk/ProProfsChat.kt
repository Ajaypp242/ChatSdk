package com.chat.sdk

import android.content.Context
import android.view.View
import com.chat.sdk.activity.bubble.Bubble
import com.chat.sdk.activity.bubble.OnlineStatus
import com.chat.sdk.activity.pre_post_chat.PrePostActivity
import com.chat.sdk.activity.pre_post_chat.PrePostViewModal
import com.chat.sdk.modal.ChatSettingData
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.session.Session
import com.chat.sdk.session.SessionKeys
import kotlinx.coroutines.*
import java.util.*

class ProProfsChat {
    private var bubble: View? = null
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    private var chatSettingData: ChatSettingData? = null
    lateinit var context: Context
    private lateinit var viewModel: PrePostViewModal
    private var delay = Session.time
    private var timer: Timer? = null
    private var operatorStatus = 0
    fun init(context: Context, site_id: String): View? {
        this.context = context
        Session().init(context)
        runBlocking {
            val data = async { getData(site_id) }
            chatSettingData = data.await()
        }
        if (chatSettingData != null) {
            val id = 12
             bubble = Bubble().createBubble(context, chatSettingData!!.chat_style)
            bubble!!.id = id
            bubble!!.setOnClickListener {
                PrePostActivity().startActivity(site_id, context, chatSettingData!!)
            }
            OnlineStatus.changeStatus(bubble!!,operatorStatus)
            timer = Timer()
            timer!!.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    getChats(site_id, chatSettingData!!.proprofs_language_id, chatSettingData!!.proprofs_session)
                }
            }, 0, delay.toLong())

            return bubble
        }
        return null
    }

    private suspend fun getData(site_id: String): ChatSettingData? {
        var settingData: ChatSettingData? = null
        try {
            val response = ApiAdapter.apiClient.getData(
                site_id, "", "", "",
                "", "", "", "", "",
                "", "", ""
            )
            settingData = response.body()
            Session().setSessionData(SessionKeys.SESSION_ID.key, settingData?.proprofs_session)
        } catch (e: Exception) {
        }
        return settingData
    }

    fun getChats(
        siteId: String,
        proprofs_language_id: String,
        proprofs_session: String,
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ApiAdapter.apiClient.getChat(
                siteId, proprofs_language_id, proprofs_session, "0",
                "330", "1", "chat_sdk", "0",
                "",
                "", ""
            )
            val operators = response.body()?.operator_status
            val newStatus = if (operators.isNullOrEmpty()) 0 else 1
            if (operatorStatus != newStatus) {
                operatorStatus = newStatus
                OnlineStatus.changeStatus(bubble!!,operatorStatus)
            }
        }
    }
    private fun onError(message: String) {
    }
}