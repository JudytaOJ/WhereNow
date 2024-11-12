package com.example.wherenow.ui.app.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TripListViewModel @Inject constructor(
    private val getAirportUseCase: GetAirportUseCase
) : ViewModel() {

    private val _navigationEvents =
        Channel<TripListNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripListViewState())
    val uiState: StateFlow<TripListViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getAirportUseCase.invoke().let { airport ->
                    _uiState.update {
                        it.copy(
                            tripList = airport,
                            cityName = airport.find { it?.city?.isNotEmpty() == true }?.city.orEmpty(),
                            countryName = airport.find { it?.country?.isNotEmpty() == true }?.country.orEmpty()
                        )
                    }
                }
            }
        }
    }

    internal fun onUiIntent(uiIntent: TripListUiIntent) {
        when (uiIntent) {
            TripListUiIntent.OnAddTrip -> onAddTrip()
            TripListUiIntent.OnChangeMode -> onChangeMode()
        }
    }

    private fun onChangeMode() {
        _navigationEvents.trySend(TripListNavigationEvent.OnChangeMode)
    }

    private fun onAddTrip() {
        _navigationEvents.trySend(TripListNavigationEvent.OnAddTrip)
    }
}