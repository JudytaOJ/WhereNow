package com.example.wherenow.ui.app.triplist.model

import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.util.StringUtils
import java.time.LocalDate

data class TripListViewState(
    val tripList: MutableList<TripListItemData?> = mutableListOf(),
    val cityName: String = StringUtils.EMPTY,
    val countryName: String = StringUtils.EMPTY,
    val date: String = StringUtils.EMPTY,
    val timeTravel: LocalDate = LocalDate.now(),
    val countDays: Int = 0,
)