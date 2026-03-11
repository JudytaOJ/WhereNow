package com.example.wherenow.ui.app.settingsmenu.flightStatistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetFeaturesStatisticsUseCase
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsUiIntent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsViewState
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FlightStatisticsViewModel(
    private val getFeaturesStatisticsUseCase: GetFeaturesStatisticsUseCase,
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase,
    private val statesProvider: StatesProvider
) : ViewModel() {
    private val _navigationEvents = Channel<FlightStatisticsNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(
        FlightStatisticsViewState(
            features = emptyList()
        )
    )
    val uiState: StateFlow<FlightStatisticsViewState> = _uiState.asStateFlow()

    init {
        loadMapFeatures()
        getStatesVisited()
    }

    internal fun onUiIntent(uiIntent: FlightStatisticsUiIntent) {
        when (uiIntent) {
            FlightStatisticsUiIntent.OnBackClicked -> _navigationEvents.trySend(FlightStatisticsNavigationEvent.OnBackNavigation)
        }
    }

    private fun loadMapFeatures() {
        viewModelScope.launch {
            val features = getFeaturesStatisticsUseCase.invoke()
            _uiState.update { it.copy(features = features) }
        }
    }

    private fun getStatesVisited() {
        viewModelScope.launch {
            val baseList = statesProvider.provide()
            val visitedStates = getStatesVisitedUseCase.invoke(baseList)
            _uiState.update {
                it.copy(
                    statedVisited = visitedStates
                        .filter { checked -> checked.isChecked }
                        .map { map -> map.text })
            }
        }
    }
}