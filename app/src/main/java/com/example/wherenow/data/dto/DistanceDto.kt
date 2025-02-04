package com.example.wherenow.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DistanceDto(
    val attributes: DistanceAttributesDto,
    val id: String,
    val type: String
) : Parcelable