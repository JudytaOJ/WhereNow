package com.example.wherenow.ui.app.triplist.model

import com.example.wherenow.database.Trip
import java.time.LocalDate

data class TripListViewState(
    val tripList: List<Trip> = emptyList(),
    val timeTravel: LocalDate = LocalDate.now(),
    val countDays: Int = 0,
)