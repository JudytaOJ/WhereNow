package com.example.wherenow.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DistanceAttributesDto(
    @SerializedName("from_airport")
    val fromAirport: DataItemDto,
    @SerializedName("to_airport")
    val toAirport: DataItemDto,
    val miles: String
) : Parcelable