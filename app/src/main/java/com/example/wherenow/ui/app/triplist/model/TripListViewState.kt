package com.example.wherenow.ui.app.triplist.model

import com.example.wherenow.database.Trip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TripListViewState(
    val tripList: ImmutableList<Trip> = persistentListOf(),
    val showBottomSheet: Boolean = false,
    val detailsId: Int? = null
)