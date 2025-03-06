package com.example.wherenow.ui.app.triptiledetails.model

import com.example.wherenow.database.trip.Trip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TripTileDetailsViewState(
    val tripList: ImmutableList<Trip> = persistentListOf(),
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val detailsId: Int? = null
)