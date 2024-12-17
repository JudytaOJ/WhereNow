package com.example.wherenow.ui.app.tripdatadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.repository.models.toItem
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsViewState
import com.example.wherenow.util.convertMillisToDate
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
    private val getAirportUseCase: GetAirportUseCase,
    private val tripListRepository: TripListRepository
) : ViewModel() {

    private val _navigationEvents = Channel<TripDataDetailsNavigationEvent>(capacity = Channel.BUFFERED)
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
            }.onFailure { _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnErrorScreen) }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    internal fun onUiIntent(uiIntent: TripDataDetailsUiIntent) {
        when (uiIntent) {
            TripDataDetailsUiIntent.OnBackNavigation -> _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnBackNavigation)
            TripDataDetailsUiIntent.OnNextClicked -> onNextClicked()

            //fields dependent on dropdown with cities
            is TripDataDetailsUiIntent.OnUpdateDepartureAirportCode -> updateDepartureAirportCode(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateDepartureAirportName -> updateDepartureAirportName(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateDepartureCountry -> updateDepartureCountry(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateDepartureCity -> updateDepartureCity(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateArrivalAirportName -> updateArrivalAirportName(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateArrivalCity -> updateArrivalCity(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateArrivalCountry -> updateArrivalCountry(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateArrivalAirportCode -> updateArrivalAirportCode(uiIntent.newValue)
        }
    }

    private fun updateDepartureCity(newValue: String) =
        _uiState.update { state -> state.copy(arrivalCity = newValue, isErrorDepartureCity = false) }

    private fun updateDepartureAirportCode(newValue: String) = _uiState.update { state -> state.copy(arrivalCodeAirport = newValue) }
    private fun updateDepartureCountry(newValue: String) = _uiState.update { state -> state.copy(arrivalCountry = newValue) }
    private fun updateDepartureAirportName(newValue: String) = _uiState.update { state -> state.copy(arrivalAirport = newValue) }
    private fun updateArrivalCity(newValue: String) = _uiState.update { state -> state.copy(departureCity = newValue, isErrorArrivalCity = false) }
    private fun updateArrivalAirportCode(newValue: String) = _uiState.update { state -> state.copy(departureCodeAirport = newValue) }
    private fun updateArrivalCountry(newValue: String) = _uiState.update { state -> state.copy(departureCountry = newValue) }
    private fun updateArrivalAirportName(newValue: String) = _uiState.update { state -> state.copy(departureAirport = newValue) }

    private fun onNextClicked() {
        if (_uiState.value.arrivalCity.isEmpty()) _uiState.update { it.copy(isErrorDepartureCity = true) }
        if (_uiState.value.departureCity.isEmpty()) _uiState.update { it.copy(isErrorArrivalCity = true) }

        if (!_uiState.value.isErrorArrivalCity && !_uiState.value.isErrorDepartureCity) {
            val item = TripListItemData(
                departureCity = _uiState.value.departureCity,
                departureCountry = _uiState.value.departureCountry,
                date = _uiState.value.date.let { it.convertMillisToDate(it) },
                departureAirport = _uiState.value.departureAirport,
                departureCodeAirport = _uiState.value.departureCodeAirport,
                arrivalCity = _uiState.value.arrivalCity,
                arrivalCountry = _uiState.value.arrivalCountry,
                arrivalAirport = _uiState.value.arrivalAirport,
                arrivalCodeAirport = _uiState.value.arrivalCodeAirport
            )

            viewModelScope.launch {
                runCatching {
                    tripListRepository.saveDataTile(trip = item.toItem())
                    _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnNextClicked)
                }
            }
        }
    }
}