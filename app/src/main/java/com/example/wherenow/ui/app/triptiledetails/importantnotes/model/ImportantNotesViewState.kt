package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

import com.example.wherenow.database.notes.Notes

data class ImportantNotesViewState(
    val isLoading: Boolean = false,
    val notesList: List<Notes> = listOf()
)