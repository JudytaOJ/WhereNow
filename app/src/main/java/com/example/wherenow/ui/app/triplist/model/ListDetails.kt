package com.example.wherenow.ui.app.triplist.model

import com.example.wherenow.data.dto.DataItemDto

data class ListDetails(
    val city: String,
    val country: String,
    val iata: String,
    val icao: String,
    val name: String
)

fun DataItemDto.toDto(): ListDetails =
    ListDetails(
        city = city,
        country = country,
        iata = iata,
        icao = icao,
        name = name
    )