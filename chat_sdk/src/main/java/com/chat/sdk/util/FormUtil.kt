package com.chat.sdk.util

import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R
import com.chat.sdk.activity.form.FormFieldType
import com.chat.sdk.modal.*

class FormUtil {
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

    fun validateForm(chatFormFields: List<ChatFormField>,  recyclerView: RecyclerView ): FormValidationReturnType {
        val submitData = ArrayList<FormValidationSubmitType>()
        var isValid = true
        for ((index,field) in chatFormFields.withIndex()) {
            if (field.js == "Y") {
                if (field.isemail == "Y") {
                    if(field.value.isNullOrEmpty()){
                        isValid = false
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility = TextView.VISIBLE
                        break
                    } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(field.value.toString()).matches()){
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility = TextView.VISIBLE
                        isValid = false
                        break
                    } else {
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility = TextView.INVISIBLE
                        isValid = true
                    }
                }  else {
                    if(field.value.isNullOrEmpty()){
                        isValid = false
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility = TextView.VISIBLE
                        break
                    }else {
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility = TextView.INVISIBLE
                        isValid = true
                    }
                }
            }
        }
        return FormValidationReturnType(submitData, isValid)
    }

    fun getFormValues(chatFormFields: List<ChatFormField>): FormSubmitValue {
        val dynamicStringParams: HashMap<String, String> = HashMap()
        val dynamicArrayParams: HashMap<String, ArrayList<String>> = HashMap()
        val name = ""
        val email = ""
        val formSubmitValue =FormSubmitValue(name,email,dynamicStringParams,dynamicArrayParams)
        for ((index, field) in chatFormFields.withIndex()) {
            when (field.fld_type) {
                FormFieldType.TEXT.type -> {
                    if (field.isname == "Y") {
                        formSubmitValue.name = field.value.toString()
                    } else if (field.isemail == "Y") {
                        formSubmitValue.email = field.value.toString()
                    } else {
                        dynamicStringParams["pp_fld_${index + 1}"] = field.value.toString()
                    }
                }
                FormFieldType.TEXTAREA.type -> {
                   formSubmitValue.dynamicStringParams["pp_fld_${index + 1}"] = field.value.toString()
                }
                else -> {
                    val response = ArrayList<String>()
                    val res = field.value?.split(",")
                    for (value in res!!) {
                        response.add(value)
                    }
                   formSubmitValue.dynamicArrayParams["pp_fld_${index + 1}"] = response
                }
            }
        }
        return formSubmitValue
    }
}