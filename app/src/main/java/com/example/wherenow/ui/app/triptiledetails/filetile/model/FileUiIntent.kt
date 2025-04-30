package com.example.wherenow.ui.app.triptiledetails.filetile.model

import com.example.wherenow.repository.file.models.FileData

internal sealed class FileUiIntent {
    data object OnBackClicked : FileUiIntent()
    data class AddFile(val file: FileData) : FileUiIntent()
    data class OpenFile(val id: FileData) : FileUiIntent()
    data class OnDeleteFile(val id: Int) : FileUiIntent()
}