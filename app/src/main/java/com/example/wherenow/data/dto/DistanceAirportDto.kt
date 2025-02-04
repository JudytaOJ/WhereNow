package com.example.wherenow.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DistanceAirportDto(
    val from: String,
    val to: String
) : Parcelable