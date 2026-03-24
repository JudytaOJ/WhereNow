package com.example.wherenow.ui.app.settingsmenu.flightStatistics.models

import kotlinx.serialization.json.JsonElement

internal data class FlightStatisticsViewState(
    val features: List<JsonElement>? = null,
    val statedVisited: List<String> = listOf(),
    //Statistics
    val totalFlight: Int = 0,
    val totalDistance: Int = 0,
    val mostFrequentRoute: String? = null,
    val longestFlight: Int = 0,
    val shortestFlight: Int = 0,
    val flightsPerMonth: Int = 0,
    val topArrivalCity: String? = null,
    val topDestinationCity: String? = null
)