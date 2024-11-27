package com.example.wherenow.repository.models

import kotlinx.serialization.Serializable

@Serializable
data class TripListData(
    val tripList: MutableList<TripListItemData>
)