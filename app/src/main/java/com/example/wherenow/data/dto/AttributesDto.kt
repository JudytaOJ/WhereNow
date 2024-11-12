package com.example.wherenow.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttributesDto(
    val attributes: DataItemDto,
    val id: String,
    val type: String
) : Parcelable