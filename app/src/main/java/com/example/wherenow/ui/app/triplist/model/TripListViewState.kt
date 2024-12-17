package com.example.wherenow.ui.app.triplist.model

import com.example.wherenow.database.Trip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class TripListViewState(
    val tripList: ImmutableList<Trip> = persistentListOf(),
    val timeTravel: LocalDate = LocalDate.now(),
    val countDays: Int = 0,
    val showBottomSheet: Boolean = false,
    val detailsId: Int? = null
)