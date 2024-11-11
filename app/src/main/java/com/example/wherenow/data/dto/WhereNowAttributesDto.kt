package com.example.wherenow.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WhereNowAttributesDto(
    val attributes: WhereNowDataItemDto,
    val id: String,
    val type: String
) : Parcelable