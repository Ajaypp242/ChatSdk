package com.chat.chat_sdk

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import com.chat.chat_sdk.activity.pre_post_chat.PrePostActivity
import com.chat.chat_sdk.modal.ChatSettingData

import com.chat.chat_sdk.network.ApiAdapter
import com.chat.chat_sdk.session.Session
import com.chat.chat_sdk.session.SessionKeys
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class Bubble {

    fun init(context: Context, site_id: String): FloatingActionButton? {
        var chatSettingData: ChatSettingData? = null
        Session().init(context)
        runBlocking {
            val data = async { getData(site_id) }
            chatSettingData = data.await()
        }
        if(chatSettingData != null){
            return  createBubble(site_id, context, chatSettingData!!)
        }
        return  null
    }

    private fun createBubble(site_id: String, context: Context, chatSettingData: ChatSettingData): FloatingActionButton {
        val floatingActionButton = FloatingActionButton(context)
        floatingActionButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#${chatSettingData.chat_style.chead_color}"))
        floatingActionButton.setImageResource(R.drawable.third)
        floatingActionButton.setColorFilter( Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        floatingActionButton.setOnClickListener {
            PrePostActivity().startActivity(site_id, context, chatSettingData)
        }
        return floatingActionButton
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