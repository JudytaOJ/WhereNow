package com.example.wherenow.data.usecases

import android.net.Uri
import com.example.wherenow.database.file.domain.FileNameResolver

class GetFileNameUseCase internal constructor(
    private val fileNameResolver: FileNameResolver
) {
    operator fun invoke(uri: Uri): String {
        return fileNameResolver.resolve(uri)
    }
}