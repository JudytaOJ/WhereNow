package com.example.wherenow.ui.app.tripdatadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsUiIntent
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
internal class TripDataDetailsViewModel @Inject constructor(
    private val getAirportUseCase: GetAirportUseCase
) : ViewModel() {

    private val _navigationEvents =
        Channel<TripDataDetailsNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripDataDetailsViewState())
    val uiState: StateFlow<TripDataDetailsViewState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                getAirportUseCase.invoke().let { airport ->
                    val cityListItem = airport.find { it?.airportList?.isNotEmpty() == true }?.airportList
                    _uiState.update {
                        it.copy(
                            cityList = cityListItem ?: emptyList()
                        )
                    }
                }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    internal fun onUiIntent(uiIntent: TripDataDetailsUiIntent) {
        when (uiIntent) {
            TripDataDetailsUiIntent.OnBackNavigation -> _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnBackNavigation)
        }
    }
}