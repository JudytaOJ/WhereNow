package com.example.wherenow.ui.app.triptiledetails.filetile.model

import com.example.wherenow.repository.file.models.FileData

internal sealed class FileNavigationEvent {
    data object OnBackClicked : FileNavigationEvent()
    data class OnOpenFile(val file: FileData) : FileNavigationEvent()
}