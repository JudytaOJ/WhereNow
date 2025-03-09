package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData

internal sealed class ImportantNotesUiIntent {
    data object OnBackClicked : ImportantNotesUiIntent()
    data class OnAddNotes(val tripId: Int) : ImportantNotesUiIntent()
    data class OnDeleteNote(val id: Int) : ImportantNotesUiIntent()
    data class OnEditNote(val note: ImportantNoteItemData) : ImportantNotesUiIntent()
}