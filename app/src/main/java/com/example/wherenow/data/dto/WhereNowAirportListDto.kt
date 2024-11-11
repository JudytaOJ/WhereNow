package com.example.wherenow.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WhereNowAirportListDto(
    @SerializedName("data")
    val airportList: List<WhereNowAttributesDto>
) : Parcelable