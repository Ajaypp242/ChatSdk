package com.chat.sdk.activity.chat

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chat.sdk.ProProfsChat
import com.chat.sdk.R
import com.chat.sdk.modal.ChatStyle
import com.chat.sdk.modal.Message
import com.chat.sdk.network.BaseUrl

internal class ChatAdapter(private val chatStyle: ChatStyle, private val context: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val operatorMessageView = 1
    private val visitorMessageView = 2

    var messages: ArrayList<Message>? = null

    fun setChatList(messages: ArrayList<Message>) {
        this.messages = messages
    }

    fun addChatList(messages: ArrayList<Message>) {
        this.messages?.addAll(messages)
    }

    fun updateVisitorLastMessageId(id:String){
        if(messages != null){
            this.messages!!.get(messages!!.size -1).sno = id
            notifyDataSetChanged()
        }
    }

    fun updateVisitorMessageStatus(messageId:String){
        val messageIndex = this.messages?.indexOfFirst { it.sno == messageId }
        this.messages!![messageIndex!!].msg_status = "2"
        notifyDataSetChanged()
    }
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View?
        return when (viewType) {
            visitorMessageView -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.visitor_chat_view, parent, false)
                return ChatViewHolder(view)
            }
            operatorMessageView -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.operator_chat_view, parent, false)
                ChatViewHolder(view)
            }
            else -> {
                return ChatViewHolder(View(context))
            }
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        if (holder.itemViewType == visitorMessageView || holder.itemViewType == operatorMessageView) {
            val message: TextView = holder.itemView.findViewById(R.id.message)
            val time: TextView = holder.itemView.findViewById(R.id.time)
            val image: ImageView = holder.itemView.findViewById(R.id.image)

            if (holder.itemViewType == visitorMessageView) {
                message.setTextColor(Color.parseColor("#${chatStyle.chat_visitor_name_color}"))
                val unwrappedDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.visitor_chat_bubble)
                val wrappedDrawable = unwrappedDrawable?.let { DrawableCompat.wrap(it) }
                if (wrappedDrawable != null) {
                    DrawableCompat.setTint(
                        wrappedDrawable,
                        Color.parseColor("#${chatStyle.chead_color}")
                    )
                }
                message.background = wrappedDrawable
                image.background = wrappedDrawable
                val status: TextView = holder.itemView.findViewById(R.id.status)
                val statusVal = messages?.get(position)?.msg_status
                if(statusVal == "2"){
                    status.text = "Seen"
                    status.setTextColor(Color.parseColor("#11e711"))
                } else {
                    status.text = "Sent"
                    status.setTextColor(Color.GRAY)
                }

            } else {
                message.setTextColor(Color.parseColor("#${chatStyle.chat_operator_name_color}"))
            }
            val item = messages?.get(position)
            if(item?.rand_no == "i"){
                image.visibility = ImageView.VISIBLE
                message.visibility = TextView.GONE
                    Glide.with(context)
                    .load("${BaseUrl.messageImageUrl}${ProProfsChat.account_id}/${item.message}")
                    .centerCrop()
                    .into(image)
            } else {
                message.text = item?.message
                message.movementMethod = LinkMovementMethod.getInstance()
                message.visibility = TextView.VISIBLE
                image.visibility = ImageView.GONE
            }

            if (chatStyle.addchtm_time == "Y") {
                time.visibility = View.VISIBLE
            } else {
                time.visibility = View.GONE
            }
            time.text = item?.msgtm
        }
    }

    override fun getItemCount(): Int {
        if (messages != null) {
            return messages!!.size
        }
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages?.get(position)?.v_o) {
            "v" -> {
                visitorMessageView
            }
            "o" -> {
                operatorMessageView
            }
            else -> {
                val view = View(context)
                view.id = R.id.empty_view
                view.id
            }
        }
    }
}