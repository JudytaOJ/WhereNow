package com.example.wherenow.ui.app.settingsmenu.flightStatistics

import androidx.lifecycle.ViewModel
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsUiIntent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

internal class FlightStatisticsViewModel : ViewModel() {
    private val _navigationEvents = Channel<FlightStatisticsNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(FlightStatisticsViewState())
    val uiState: StateFlow<FlightStatisticsViewState> = _uiState.asStateFlow()

    internal fun onUiIntent(uiIntent: FlightStatisticsUiIntent) {
        when (uiIntent) {
            FlightStatisticsUiIntent.OnBackClicked -> TODO()
        }
    }
}