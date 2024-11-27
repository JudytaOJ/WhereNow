package com.example.wherenow.repository.models

import kotlinx.serialization.Serializable

@Serializable
data class TripListItemData(
    val date: String,
    val city: String,
    val country: String
)