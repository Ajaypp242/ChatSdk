package com.chat.sdk.util

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R
import com.chat.sdk.activity.form.FormFieldType
import com.chat.sdk.modal.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

internal class FormUtil {
    fun getCatTypeFields(
        data: List<ChatFormField>,
        chatWindowType: FormType
    ): List<ChatFormField> {
        return data.filter { it.fleg == chatWindowType.type }
    }

    fun getCatTextField(data: List<ChatFormText>, chatWindowType: FormType): ChatFormText {
        return data.filter { it.fleg == chatWindowType.type }[0]
    }

    fun sortFieldData(data: List<ChatFormField>): List<ChatFormField> {
        return data.sortedBy { it.order.toInt() }
    }

    fun validateForm(
        chatFormFields: List<ChatFormField>,
        recyclerView: RecyclerView
    ): FormValidationReturnType {
        val submitData = ArrayList<FormValidationSubmitType>()
        var isValid = true
        for ((index, field) in chatFormFields.withIndex()) {
            if (field.js == "Y") {
                if (field.isemail == "Y") {
                    if (field.value.isNullOrEmpty()) {
                        isValid = false
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility =
                            TextView.VISIBLE
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(field.value.toString())
                            .matches()
                    ) {
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility =
                            TextView.VISIBLE
                        isValid = false
                    } else {
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility =
                            TextView.GONE
                    }
                } else {
                    if (field.value.isNullOrEmpty()) {
                        isValid = false
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility =
                            TextView.VISIBLE
                    } else {
                        recyclerView[index].findViewById<TextView>(R.id.err_msg).visibility =
                            TextView.GONE
                    }
                }
            }
        }
        return FormValidationReturnType(submitData, isValid)
    }

    fun getFormValues(chatFormFields: List<ChatFormField>, dynamic_field_tag:String): FormSubmitValue {
        val dynamicStringParams: HashMap<String, String> = HashMap()
        val dynamicArrayParams: HashMap<String, ArrayList<String>> = HashMap()
        val name = ""
        val email = ""
        val formSubmitValue = FormSubmitValue(name, email, dynamicStringParams, dynamicArrayParams)
        for ((index, field) in chatFormFields.withIndex()) {
            when (field.fld_type) {
                FormFieldType.TEXT.type -> {
                    if (field.isname == "Y") {
                        formSubmitValue.name = field.value.toString()
                    } else if (field.isemail == "Y") {
                        formSubmitValue.email = field.value.toString()
                    } else {
                        dynamicStringParams["${dynamic_field_tag}${index + 1}"] = field.value.toString()
                    }
                }
                FormFieldType.TEXTAREA.type -> {
                    formSubmitValue.dynamicStringParams["${dynamic_field_tag}${index + 1}"] =
                        field.value.toString()
                }
                else -> {
                    val response = ArrayList<String>()
                    if(field.value != null){
                        val res = field.value?.split(",")
                        for (value in res!!) {
                            response.add(value)
                        }
                        formSubmitValue.dynamicArrayParams["${dynamic_field_tag}${index + 1}"] = response
                    }
                }
            }
        }
        return formSubmitValue
    }

    fun starRating(count: Int, context: Context, color: String, stars: kotlin.Array<ImageView>, resId:Int) {
        val fillUnwrappedDrawable =
            AppCompatResources.getDrawable(context, resId)
        val fillWrappedDrawable = fillUnwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (fillWrappedDrawable != null) {
            DrawableCompat.setTint(
                fillWrappedDrawable,
                Color.parseColor("#${color}")
            )
        }

        val normalUnwrappedDrawable =
            AppCompatResources.getDrawable(context, resId)
        val normalWrappedDrawable = normalUnwrappedDrawable?.let { DrawableCompat.wrap(it) }
        if (normalWrappedDrawable != null) {
            DrawableCompat.setTint(
                normalWrappedDrawable,
                Color.parseColor("#e3dddd")
            )
        }

        for ((index, field) in stars.withIndex()) {
            if (index < count) {
                field.background = fillWrappedDrawable
            } else {
                field.background = normalWrappedDrawable
            }
        }

    }
}