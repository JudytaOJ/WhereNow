package com.example.wherenow.repository.models

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import kotlinx.serialization.Serializable

@Serializable
data class TripListItemData(
    val date: Long,
    val image: Int,
    val departureCity: String,
    val departureCountry: String,
    val departureAirport: String,
    val arrivalCity: String,
    val arrivalCountry: String,
    val arrivalAirport: String,
    val arrivalCodeAirport: String,
    val departureCodeAirport: String,
    val distance: String,
    val id: Int
)

fun TripListItemData.toItem(): Trip = Trip(
    date = date,
    image = image.let { WhereNowDetailsTileImageType.entries.random().icon },
    departureCity = departureCity,
    departureCountry = departureCountry,
    departureAirport = departureAirport,
    arrivalCity = arrivalCity,
    arrivalCountry = arrivalCountry,
    arrivalAirport = arrivalAirport,
    arrivalCodeAirport = arrivalCodeAirport,
    departureCodeAirport = departureCodeAirport,
    distance = distance,
    id = id
)