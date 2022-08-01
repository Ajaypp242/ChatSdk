package com.chat.sdk.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chat.sdk.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal class CommonUtil {

    fun customLoadingDialogAlert(context: Context, layoutInflater: LayoutInflater, heading:String, color:String): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.custom_loader,null)
        view.findViewById<TextView>(R.id.text).text =  heading
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById<ProgressBar>(R.id.progress_circular).indeterminateTintList = ColorStateList.valueOf(Color.parseColor("#${color}"))
        }
        builder.setView(view)
        builder.setCancelable(false)
        return builder.create()
    }

    fun showProcessSpinner(context: Context, text: String): AlertDialog {
        val llPadding = 60
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        // Creating a ProgressBar inside the layout
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
//      llParam.setMargins(0,0,60,0)

        // Creating a TextView inside the layout
        val tvText = TextView(context)
        tvText.text = text
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)

        // Setting the AlertDialog Builder view
        // as the Linear layout created above
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        // Displaying the dialog

        return builder.create()
    }

    fun getTimeZone(): Int {
        val timezone = Date().timezoneOffset
        Log.d("timezone", (-(timezone)).toString())
        if(timezone != 0){
            return -(timezone)
        }
        return 0
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeZone2(): String {
        val calendar = Calendar.getInstance(
            TimeZone.getTimeZone("GMT"),
            Locale.getDefault()
        )
        val currentLocalTime = calendar.time
        val date: DateFormat = SimpleDateFormat("Z")
        return date.format(currentLocalTime).trim('+')
    }

    fun getCurrentTime(): String {
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm a")
        return dateFormat.format(Date())
    }

}