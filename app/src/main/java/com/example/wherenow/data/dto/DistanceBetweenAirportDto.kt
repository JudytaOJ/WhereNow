package com.example.wherenow.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DistanceBetweenAirportDto(
    @SerializedName("data")
    val distanceAirportList: DistanceDto
) : Parcelable