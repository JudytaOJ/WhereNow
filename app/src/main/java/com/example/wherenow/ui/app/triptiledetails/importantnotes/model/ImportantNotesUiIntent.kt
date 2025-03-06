package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

internal sealed class ImportantNotesUiIntent {
    data object OnBackClicked : ImportantNotesUiIntent()
    data object OnAddNotes : ImportantNotesUiIntent()
    data class OnDeleteNote(val id: Int) : ImportantNotesUiIntent()
}