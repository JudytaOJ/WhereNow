package com.example.wherenow.repository.importantnotes.models

import com.example.wherenow.database.notes.Notes
import kotlinx.serialization.Serializable

@Serializable
data class ImportantNoteItemData(
    val title: String,
    val description: String,
    val id: Int,
    val tripId: Int
)

fun ImportantNoteItemData.toItem(): Notes = Notes(
    title = title,
    description = description,
    id = id,
    tripId = tripId
)