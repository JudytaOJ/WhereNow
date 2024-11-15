package com.example.wherenow.ui.app.tripdatadetails

import com.example.wherenow.data.dto.AttributesDto

internal data class TripDataDetailsViewState(
    val isLoading: Boolean = false,
    val cityList: List<AttributesDto> = emptyList()
)