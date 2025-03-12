package com.example.wherenow.ui.app.triplist.model

import com.example.wherenow.database.trip.Trip

data class TripListViewState(
    val tripList: List<Trip> = listOf(),
    val optionsList: List<TripListDataEnum> = listOf(TripListDataEnum.PAST, TripListDataEnum.PRESENT, TripListDataEnum.FUTURE),
    val selectedButtonType: TripListDataEnum = TripListDataEnum.PRESENT,
    val isLoading: Boolean = false
)