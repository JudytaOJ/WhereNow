package com.example.wherenow.repository.models

import com.example.wherenow.database.Trip
import kotlinx.serialization.Serializable

@Serializable
data class TripListItemData(
    val date: String,
    val departureCity: String,
    val departureCountry: String,
    val departureAirport: String,
    val arrivalCity: String,
    val arrivalCountry: String,
    val arrivalAirport: String,
    val arrivalCodeAirport: String,
    val departureCodeAirport: String
)

fun TripListItemData.toItem(): Trip = Trip(
    date = date,
    departureCity = departureCity,
    departureCountry = departureCountry,
    departureAirport = departureAirport,
    arrivalCity = arrivalCity,
    arrivalCountry = arrivalCountry,
    arrivalAirport = arrivalAirport,
    arrivalCodeAirport = arrivalCodeAirport,
    departureCodeAirport = departureCodeAirport
)