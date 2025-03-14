package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData

data class ImportantNotesViewState(
    val isLoading: Boolean = false,
    val notesList: List<ImportantNoteItemData> = listOf(),
    val tripId: Int = 0
)