package com.chat.chat_sdk.activity.pre_post_chat

import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chat.chat_sdk.R
import com.chat.chat_sdk.modal.ChatFormField
import com.chat.chat_sdk.modal.Message

class PrePostAdapter() :
    RecyclerView.Adapter<PrePostAdapter.PrePostViewHolder>() {
    private var chatFormField: List<ChatFormField>? = null

    fun setFormFields(chatFormFields: List<ChatFormField>) {
        this.chatFormField = chatFormFields
    }

    inner class PrePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val value: EditText = itemView.findViewById(R.id.value)
        val errs: TextView = itemView.findViewById(R.id.err_msg)
        val fldType: TextView = itemView.findViewById(R.id.fld_type)
        val fldName: TextView = itemView.findViewById(R.id.fld_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrePostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pre_post_item, parent, false)
        return PrePostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrePostViewHolder, position: Int) {
        val item = chatFormField?.get(position)
        holder.title.text = item?.fld_name
        holder.errs.text = item?.jsmsg
        holder.fldType.text = item?.fld_type
        holder.fldName.text = item?.fld_name
        if (item?.fld_type == "textarea") {
            holder.value.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            holder.value.setLines(5)
        }
    }

    override fun getItemCount(): Int {
        Log.d("chatFormField",chatFormField.toString())
        if(chatFormField != null){
            return  chatFormField!!.size
        }
        return  0
    }
}