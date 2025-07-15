package com.example.wherenow.data.resolver

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.wherenow.database.file.domain.FileNameResolver

class AndroidFileNameResolver(private val context: Context) : FileNameResolver {
    override fun resolve(uri: Uri): String {
        var name = "file.pdf"
        val cursor = context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }

        return name
    }
}