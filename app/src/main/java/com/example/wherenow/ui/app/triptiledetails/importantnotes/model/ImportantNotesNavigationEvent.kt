package com.example.wherenow.ui.app.triptiledetails.importantnotes.model

internal sealed class ImportantNotesNavigationEvent {
    data object OnBack : ImportantNotesNavigationEvent()
    data object OnAddNotes : ImportantNotesNavigationEvent()
}