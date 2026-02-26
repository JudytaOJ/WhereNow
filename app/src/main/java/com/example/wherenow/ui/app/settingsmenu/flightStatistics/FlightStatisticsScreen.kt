package com.example.wherenow.ui.app.settingsmenu.flightStatistics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsNavigationEvent
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_FLIGHT_STATISTICS_KEY = "NavigationFlightStatisticsEvents"

@Composable
internal fun FlightStatisticsScreen(
    navigationEvent: (FlightStatisticsNavigationEvent) -> Unit
) {
    val viewModel: FlightStatisticsViewModel = koinViewModel()
    FlightStatisticsContent()
    LaunchedEffect(NAVIGATION_FLIGHT_STATISTICS_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
internal fun FlightStatisticsContent() {
    Text("TODO")
}