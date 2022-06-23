package com.chat.sdk.activity.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R
import com.chat.sdk.modal.Message

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val operatorMessageView = 1
    private val visitorMessageView = 2

    var messages: ArrayList<Message>? = null
    fun setChatList(messages: ArrayList<Message>) {
        this.messages = messages
    }


    fun addChatList(messages: ArrayList<Message>) {
        this.messages?.addAll(messages)
    }


    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View?
        return if (viewType == visitorMessageView) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.visitor_chat_view, parent, false)
            ChatViewHolder(view)
        } else   {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.operator_chat_view, parent, false)
            ChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = messages?.get(position)
        holder.message.text = item?.message
    }

    override fun getItemCount(): Int {
       if(messages != null){
           return  messages!!.size
       }
        return  0
    }

    override fun getItemViewType(position: Int): Int {
        if (messages?.get(position)?.v_o == "v") {
            return visitorMessageView
        } else {
            return operatorMessageView
        }
    }
}