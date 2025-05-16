package com.example.wherenow.database.file.domain

import android.net.Uri

interface FileNameResolver {
    fun resolve(uri: Uri): String
}