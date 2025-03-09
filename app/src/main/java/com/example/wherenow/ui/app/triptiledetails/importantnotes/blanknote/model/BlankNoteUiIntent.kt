package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model

import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData

internal sealed class BlankNoteUiIntent {
    data object OnBackClicked : BlankNoteUiIntent()
    data class NextClickedAddOrEditNote(val note: ImportantNoteItemData) : BlankNoteUiIntent()
    data class OnUpdateTitleNote(val newValue: String) : BlankNoteUiIntent()
    data class OnUpdateDescriptionNote(val newValue: String) : BlankNoteUiIntent()
}