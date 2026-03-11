package com.example.wherenow.ui.app.settingsmenu.flightStatistics.models

import kotlinx.serialization.json.JsonElement

internal data class FlightStatisticsViewState(
    val features: List<JsonElement>? = null,
    val statedVisited: List<String> = listOf()
)