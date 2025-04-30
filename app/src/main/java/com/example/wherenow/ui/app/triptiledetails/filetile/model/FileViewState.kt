package com.example.wherenow.ui.app.triptiledetails.filetile.model

import com.example.wherenow.repository.file.models.FileData

internal data class FileViewState(
    val fileList: List<FileData> = listOf(),
    val tripId: Int = 0
)