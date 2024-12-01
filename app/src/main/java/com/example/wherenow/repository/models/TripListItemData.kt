package com.example.wherenow.repository.models

import com.example.wherenow.database.Trip
import kotlinx.serialization.Serializable

@Serializable
data class TripListItemData(
    val date: String,
    val city: String,
    val country: String
)

fun TripListItemData.toItem(): Trip = Trip(
    date = date,
    cityFrom = city,
    countryFrom = country
)