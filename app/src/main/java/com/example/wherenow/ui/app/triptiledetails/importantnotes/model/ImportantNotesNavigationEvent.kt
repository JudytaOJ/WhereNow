package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData

internal sealed class ImportantNotesNavigationEvent {
    data object OnBack : ImportantNotesNavigationEvent()
    data class OnAddNotes(val tripId: Int) : ImportantNotesNavigationEvent()
    data class OnEditNote(val note: ImportantNoteItemData) : ImportantNotesNavigationEvent()
}