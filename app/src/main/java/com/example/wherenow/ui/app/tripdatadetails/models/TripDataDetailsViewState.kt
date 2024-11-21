package com.example.wherenow.ui.app.tripdatadetails.models

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.util.StringUtils

internal data class TripDataDetailsViewState(
    val isLoading: Boolean = false,
    val cityList: List<AttributesDto> = emptyList(),
    val fromCityName: String = StringUtils.EMPTY,
    val fromCountryName: String = StringUtils.EMPTY,
    val fromIata: String = StringUtils.EMPTY,
    val fromAirportName: String = StringUtils.EMPTY,
    val toCityName: String = StringUtils.EMPTY,
    val toCountryName: String = StringUtils.EMPTY,
    val toIata: String = StringUtils.EMPTY,
    val toAirportName: String = StringUtils.EMPTY,
    val date: Long = 0
)