package com.example.wherenow.ui.triplist

import androidx.lifecycle.ViewModel
import com.example.wherenow.ui.triplist.model.WhereNowTripListNavigationEvent
import com.example.wherenow.ui.triplist.model.WhereNowTripListUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class WhereNowTripListViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvents =
        Channel<WhereNowTripListNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(
        WhereNowTripListViewState(
            tripList = listOf(
                Test(
                    cityName = "Lizbona",
                    countryName = "Portugalia",
                    date = "20 sierpień 2024",
                    timeTravel = LocalDate.now().minusDays(4),
                    countDays = 0
                ),
                Test(
                    cityName = "Dżakart",
                    countryName = "Indonezja",
                    date = "20 styczeń 2025",
                    timeTravel = LocalDate.now(),
                    countDays = 49
                )
            )
        )
    )
    val uiState: StateFlow<WhereNowTripListViewState> = _uiState.asStateFlow()

    internal fun onUiIntent(uiIntent: WhereNowTripListUiIntent) {
        when (uiIntent) {
            WhereNowTripListUiIntent.OnAddTrip -> onAddTrip()
            WhereNowTripListUiIntent.OnChangeMode -> onChangeMode()
        }
    }

    private fun onChangeMode() {
        _navigationEvents.trySend(WhereNowTripListNavigationEvent.OnChangeMode)
    }

    private fun onAddTrip() {
        _navigationEvents.trySend(WhereNowTripListNavigationEvent.OnAddTrip)
    }
}