package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model

internal sealed class BlankNoteNavigationEvent {
    data object OnBackClicked : BlankNoteNavigationEvent()
    data class AddClickedEvent(val tripId: Int) : BlankNoteNavigationEvent()
}