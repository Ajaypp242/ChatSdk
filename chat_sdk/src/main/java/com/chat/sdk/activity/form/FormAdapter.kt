package com.chat.sdk.activity.form

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R
import com.chat.sdk.modal.ChatFormField

internal class FormAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ChildAdaptorResponse {
    var chatFormField: List<ChatFormField>? = null
    private var context: Context? = null
    fun setFormFields(chatFormFields: List<ChatFormField>, context: Context) {
        this.context = context
        this.chatFormField = chatFormFields
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (chatFormField != null) {
            return chatFormField!!.size
        }
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (chatFormField!![position].fld_type) {
            FormFieldType.TEXT.type -> {
                return R.layout.form_filed_text
            }
            FormFieldType.TEXTAREA.type -> {
                return R.layout.form_field_textarea
            }
            FormFieldType.RADIO.type -> {
                return R.layout.form_filed_radio
            }
            FormFieldType.CHECKBOX.type -> {
                return R.layout.form_filed_checkbox
            }
            FormFieldType.DROPDOWN.type -> {
                return R.layout.form_field_dropdown
            }
            FormFieldType.PRIVACY_POLICY.type -> {
                return R.layout.form_filed_checkbox
            }
            else -> {
                R.layout.form_filed_text
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val text = chatFormField?.get(position)?.fld_name!!
        val spannedText =  HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.itemView.findViewById<TextView>(R.id.fld_name).text = HtmlCompat.fromHtml(spannedText.toString(),HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.itemView.findViewById<TextView>(R.id.err_msg).text = chatFormField?.get(position)?.jsmsg
        val requiredIcon =  holder.itemView.findViewById<TextView>(R.id.required_icon)
        if(chatFormField?.get(position)?.js == "Y"){
            requiredIcon.visibility = TextView.VISIBLE
        } else {
            requiredIcon.visibility = TextView.GONE
        }
        when (holder.itemViewType) {
            R.layout.form_filed_radio -> {
                val recyclerView =
                    holder.itemView.findViewById<RecyclerView>(R.id.radio_item_recycler_view)
                val layoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = layoutManager
                val adapter = RadioAdapter(this)
                recyclerView.adapter = adapter
                val options = chatFormField!![position].sel_item.split(",")
                adapter.setOptions(options, position)
            }
            R.layout.form_filed_checkbox -> {
                val recyclerView =
                    holder.itemView.findViewById<RecyclerView>(R.id.checkbox_item_recycler_view)
                val layoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = layoutManager
                val adapter = CheckboxAdapter(this)
                recyclerView.adapter = adapter
                val options = chatFormField!![position].sel_item.split(",")
                adapter.setOptions(options, position)
            }
            R.layout.form_field_dropdown -> {
                val select = holder.itemView.findViewById<TextView>(R.id.value)
                 select.setOnClickListener { it ->
                    val options = chatFormField!![position].sel_item.split(",").toTypedArray()
                    openDropdownDialog(options, it.context, position, select)
                }
            }
            else -> {
                holder.itemView.findViewById<TextView>(R.id.text)
                    .addTextChangedListener(afterTextChanged = {
                        chatFormField!![position].value = it.toString()
                    })
            }
        }
    }

    private fun openDropdownDialog(data: Array<String>, context: Context, filedPosition: Int, item:TextView) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setItems(data) { _, pos ->
            chatFormField!![filedPosition].value = data[pos]
            item.text = data[pos]
        }
        alertDialog.show()
    }

    override fun setResponse(value: String, position: Int) {
        if (position != -1 && value.isNotBlank()) {
            chatFormField!![position].value = value
        }
    }
}