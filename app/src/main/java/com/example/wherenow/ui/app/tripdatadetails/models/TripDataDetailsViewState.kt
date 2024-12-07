package com.example.wherenow.ui.app.tripdatadetails.models

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.util.StringUtils

internal data class TripDataDetailsViewState(
    val isLoading: Boolean = false,
    val cityList: List<AttributesDto> = emptyList(),
    val departureCityName: String = StringUtils.EMPTY,
    val departureCountryName: String = StringUtils.EMPTY,
    val departureIata: String = StringUtils.EMPTY,
    val departureAirportName: String = StringUtils.EMPTY,
    val arrivalCityName: String = StringUtils.EMPTY,
    val arrivalCountryName: String = StringUtils.EMPTY,
    val arrivalIata: String = StringUtils.EMPTY,
    val arrivalAirportName: String = StringUtils.EMPTY,
    val date: Long = 0
)