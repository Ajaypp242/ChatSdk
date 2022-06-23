package com.chat.sdk.util

import android.widget.EditText
import android.widget.TextView
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R
import com.chat.sdk.modal.*

class Util {
    fun getCatTypeFields(
        data: List<ChatFormField>,
        chatWindowType: ChatWindowType
    ): List<ChatFormField> {
        return data.filter { it.fleg == chatWindowType.type }
    }

    fun getCatTextField(data: List<ChatFormText>, chatWindowType: ChatWindowType): ChatFormText {
        return data.filter { it.fleg == chatWindowType.type }[0]
    }

    fun sortFieldData(data: List<ChatFormField>): List<ChatFormField> {
        return data.sortedBy { it.order.toInt() }
    }

    fun validatePrePostFields(recyclerView: RecyclerView): PrePostValidateReturnType {
        val submitData = ArrayList<PrePostValidateSubmitType>()
        var isValid = true
        for (item in recyclerView) {
            val value = item.findViewById<EditText>(R.id.value).text.toString()
            val key = item.findViewById<TextView>(R.id.title).text.toString()
            val fieldName = item.findViewById<TextView>(R.id.fld_name).text.toString()
            val param = PrePostValidateSubmitType(key, value)
            submitData.add(param)
            if (value.isEmpty()) {
                item.findViewById<TextView>(R.id.err_msg).visibility = TextView.VISIBLE
                isValid = false

            } else if (fieldName == "Email" && !android.util.Patterns.EMAIL_ADDRESS.matcher(value)
                    .matches()
            ) {
                item.findViewById<TextView>(R.id.err_msg).visibility = TextView.VISIBLE
                isValid = false
            } else {
                item.findViewById<TextView>(R.id.err_msg).visibility = TextView.GONE
            }
        }
        return PrePostValidateReturnType(submitData, isValid)
    }
}