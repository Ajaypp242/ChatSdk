package com.chat.sdk.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.*

internal class FileUtils {

    fun uriToBase64(uri: Uri, context: Context): String? {
        try {
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace()
        }
        return null
    }

    fun getFileExtension(uri: Uri, context: Context): String? {
        val contentResolver = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    fun base64ToBitmap(string: String?): Bitmap? {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}