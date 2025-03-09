package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model

import com.example.wherenow.util.StringUtils

data class BlankNoteViewState(
    val titleNote: String = StringUtils.EMPTY,
    val descriptionNote: String = StringUtils.EMPTY,
    val id: Int = 0,
    val tripId: Int = 0
)