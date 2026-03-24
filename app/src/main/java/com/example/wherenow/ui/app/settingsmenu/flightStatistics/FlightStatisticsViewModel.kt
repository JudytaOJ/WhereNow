package com.example.wherenow.ui.app.settingsmenu.flightStatistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetFeaturesStatisticsUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsUiIntent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsViewState
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesProvider
import com.example.wherenow.util.StringUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId

internal class FlightStatisticsViewModel(
    private val getFeaturesStatisticsUseCase: GetFeaturesStatisticsUseCase,
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase,
    private val statesProvider: StatesProvider,
    private val getPastTripListUseCase: GetPastTripListUseCase
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
        loadData()
    }

    internal fun onUiIntent(uiIntent: FlightStatisticsUiIntent) {
        when (uiIntent) {
            FlightStatisticsUiIntent.OnBackClicked -> _navigationEvents.trySend(FlightStatisticsNavigationEvent.OnBackNavigation)
        }
    }

    private fun loadData() {
        loadMapFeatures()
        getStatesVisited()
        loadTotalFlightDetails()
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

    private fun loadTotalFlightDetails() {
        viewModelScope.launch {
            getPastTripListUseCase.invoke().let { pastTrip ->
                _uiState.update {
                    it.copy(
                        totalFlight = pastTrip.size,
                        totalDistance = totalDistanceFlightStatistics(pastTrip),
                        mostFrequentRoute = mostFrequentRouteStatistics(pastTrip),
                        longestFlight = longestFlightStatistics(pastTrip),
                        shortestFlight = shortestFlightStatistics(pastTrip),
                        flightsPerMonth = flightsThisMonth(pastTrip),
                        topArrivalCity = topArrivalCityStatistics(pastTrip),
                        topDestinationCity = topDestinationCityStatistics(pastTrip)
                    )
                }
            }
        }
    }

    private fun topArrivalCityStatistics(pastTrip: List<TripListItemData>) = pastTrip
        .groupingBy { it.arrivalCity }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key

    private fun topDestinationCityStatistics(pastTrip: List<TripListItemData>) = pastTrip
        .groupingBy { it.departureCity }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key

    private fun flightsThisMonth(pastTrip: List<TripListItemData>): Int {
        val now = YearMonth.now(ZoneId.of("UTC"))

        return pastTrip.count {
            val date = Instant.ofEpochMilli(it.date)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate()

            date.year == now.year && date.monthValue == now.monthValue
        }
    }

    private fun longestFlightStatistics(pastTrip: List<TripListItemData>) = pastTrip.maxByOrNull { it.distance.toDouble() }?.distance?.toInt() ?: 0

    private fun shortestFlightStatistics(pastTrip: List<TripListItemData>) = pastTrip.minByOrNull { it.distance.toDouble() }?.distance?.toInt() ?: 0

    private fun totalDistanceFlightStatistics(pastTrip: List<TripListItemData>) = pastTrip.sumOf { distance -> distance.distance.toInt() }

    private fun mostFrequentRouteStatistics(pastTrip: List<TripListItemData>): String {
        if (pastTrip.isEmpty()) return StringUtils.EMPTY

        return pastTrip
            .groupingBy { city ->
                buildString {
                    append(city.departureCity)
                    append(StringUtils.HYPHEN)
                    append(city.arrivalCity)
                }
            }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key.orEmpty()
    }
}