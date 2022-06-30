package com.chat.sdk.activity.form

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R

class CheckboxAdapter(private val childAdaptorResponse: ChildAdaptorResponse) :
    RecyclerView.Adapter<CheckboxAdapter.CheckboxViewHolder>() {
    private var options: List<String>? = null
    var response=ArrayList<String>()
    private var parentPosition: Int = -1
    fun setOptions(options: List<String>, parentPosition: Int) {
        this.options = options
        this.parentPosition = parentPosition
    }

    inner class CheckboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkbox_item, parent, false)
        return CheckboxViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (options != null) {
            return options!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: CheckboxViewHolder, position: Int) {
        val text = options!![position]
        holder.btn.text = text
        holder.btn.setOnClickListener {
//
            if (holder.btn.isChecked) {
                if (!response.contains(text)) {
                    response.add(options!![position])
                }
            } else {
                if (response.contains(text)) {
                    response.remove(options!![position])
                }
            }
            Log.d("response",response.toString())
            val value = response.joinToString(",")
//            if (value != null) {
                childAdaptorResponse.setResponse(value, parentPosition)
//            }
        }


    }
}