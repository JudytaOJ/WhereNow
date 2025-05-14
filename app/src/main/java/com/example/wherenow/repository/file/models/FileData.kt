package com.example.wherenow.repository.file.models

import com.example.wherenow.database.file.File
import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val name: String,
    val url: String,
    val id: Int = 0,
    val tripId: Int
)

fun FileData.toFile(): File = File(
    name = name,
    url = url,
    id = id,
    tripId = tripId
)