package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model

internal sealed class BlankNoteUiIntent {
    data object OnBackClicked : BlankNoteUiIntent()
    data object NextClickedAddNote : BlankNoteUiIntent()
    data class OnUpdateTitleNote(val newValue: String) : BlankNoteUiIntent()
    data class OnUpdateDescriptionNote(val newValue: String) : BlankNoteUiIntent()
}