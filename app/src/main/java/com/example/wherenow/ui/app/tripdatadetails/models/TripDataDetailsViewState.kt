package com.example.wherenow.ui.app.tripdatadetails.models

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.util.StringUtils

internal data class TripDataDetailsViewState(
    val isLoading: Boolean = false,
    val cityList: List<AttributesDto> = emptyList(),
    val arrivalCity: String = StringUtils.EMPTY,
    val arrivalCountry: String = StringUtils.EMPTY,
    val arrivalCodeAirport: String = StringUtils.EMPTY,
    val arrivalAirport: String = StringUtils.EMPTY,
    val departureCity: String = StringUtils.EMPTY,
    val departureCountry: String = StringUtils.EMPTY,
    val departureCodeAirport: String = StringUtils.EMPTY,
    val departureAirport: String = StringUtils.EMPTY,
    val date: Long = 0,
    val isErrorDepartureCity: Boolean = false,
    val isErrorArrivalCity: Boolean = false
)