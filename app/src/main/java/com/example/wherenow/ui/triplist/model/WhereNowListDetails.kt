package com.example.wherenow.ui.triplist.model

import com.example.wherenow.data.dto.WhereNowDataItemDto

data class WhereNowListDetails(
    val city: String,
    val country: String,
    val iata: String,
    val icao: String,
    val name: String
)

fun WhereNowDataItemDto.toDto(): WhereNowListDetails =
    WhereNowListDetails(
        city = city,
        country = country,
        iata = iata,
        icao = icao,
        name = name
    )