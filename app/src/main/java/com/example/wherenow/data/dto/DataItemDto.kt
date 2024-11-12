package com.example.wherenow.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataItemDto(
    val city: String,
    val country: String,
    val iata: String,
    val icao: String,
    val name: String
) : Parcelable