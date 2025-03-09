package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData

internal sealed class ImportantNotesNavigationEvent {
    data object OnBack : ImportantNotesNavigationEvent()
    data object OnAddNotes : ImportantNotesNavigationEvent()
    data class OnEditNote(val note: ImportantNoteItemData) : ImportantNotesNavigationEvent()
}