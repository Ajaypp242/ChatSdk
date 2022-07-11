package com.chat.sdk.activity.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.chat.sdk.R

internal class RadioAdapter(private val childAdaptorResponse: ChildAdaptorResponse) : RecyclerView.Adapter<RadioAdapter.RadioViewHolder>() {
    private var selectedItem = -1
    private var options: List<String>? = null
    private  var parentPosition:Int = -1
    fun setOptions(options: List<String>,parentPosition:Int) {
        this.options = options
        this.parentPosition = parentPosition
    }

    inner class RadioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn: RadioButton = itemView.findViewById(R.id.option_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.radio_item, parent, false)
        return RadioViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) {
        holder.btn.text = options!![position]
        holder.btn.isChecked = position == selectedItem
        holder.btn.setOnClickListener {
            selectedItem = holder.adapterPosition
            childAdaptorResponse.setResponse(options!![position],parentPosition)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        if (options != null) {
            return options!!.size
        }
        return 0
    }
}