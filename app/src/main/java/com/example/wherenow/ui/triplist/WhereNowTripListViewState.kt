package com.example.wherenow.ui.triplist

import com.example.wherenow.ui.triplist.model.WhereNowListDetails
import com.example.wherenow.util.StringUtils
import java.time.LocalDate

data class WhereNowTripListViewState(
    val tripList: List<WhereNowListDetails?> = emptyList(),
    val cityName: String = StringUtils.EMPTY,
    val countryName: String = StringUtils.EMPTY,
    val date: String = StringUtils.EMPTY,
    val timeTravel: LocalDate = LocalDate.now(),
    val countDays: Int = 0,
)