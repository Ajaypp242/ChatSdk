package com.chat.sdk

import android.content.Context
import android.view.View
import com.chat.sdk.activity.bubble.Bubble
import com.chat.sdk.activity.pre_post_chat.PrePostActivity
import com.chat.sdk.modal.ChatSettingData
import com.chat.sdk.network.ApiAdapter
import com.chat.sdk.session.Session
import com.chat.sdk.session.SessionKeys
import kotlinx.coroutines.*

class ProProfsChat {
    private var chatSettingData: ChatSettingData? = null
    lateinit var context: Context
    fun init(context: Context, site_id: String): View? {
        this.context = context
        Session().init(context)
        runBlocking {
            val data = async { getData(site_id) }
            chatSettingData = data.await()
        }
        if (chatSettingData != null) {
            val bubble = Bubble().createBubble(context, chatSettingData!!.chat_style)
            bubble.setOnClickListener {
                PrePostActivity().startActivity(site_id, context, chatSettingData!!)
            }
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
}