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
            TripDataDetailsUiIntent.OnNextClicked -> onNextClicked()

            //fields dependent on dropdown with cities
            is TripDataDetailsUiIntent.OnUpdateFromIata -> updateFromIata(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateFromAirportName -> updateFromAirportName(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateFromCountry -> updateFromCountry(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateFromCity -> updateFromCity(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateToAirportName -> updateToAirportName(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateToCity -> updateToCity(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateToCountry -> updateToCountry(uiIntent.newValue)
            is TripDataDetailsUiIntent.OnUpdateToIata -> updateToIata(uiIntent.newValue)
        }
    }

    private fun updateFromCity(newValue: String) = _uiState.update { state -> state.copy(departureCityName = newValue, isErrorDepartureCity = false) }
    private fun updateFromIata(newValue: String) = _uiState.update { state -> state.copy(departureIata = newValue) }
    private fun updateFromCountry(newValue: String) = _uiState.update { state -> state.copy(departureCountryName = newValue) }
    private fun updateFromAirportName(newValue: String) = _uiState.update { state -> state.copy(departureAirportName = newValue) }
    private fun updateToCity(newValue: String) = _uiState.update { state -> state.copy(arrivalCityName = newValue, isErrorArrivalCity = false) }
    private fun updateToIata(newValue: String) = _uiState.update { state -> state.copy(arrivalIata = newValue) }
    private fun updateToCountry(newValue: String) = _uiState.update { state -> state.copy(arrivalCountryName = newValue) }
    private fun updateToAirportName(newValue: String) = _uiState.update { state -> state.copy(arrivalAirportName = newValue) }

    private fun onNextClicked() {
        if (_uiState.value.departureCityName.isEmpty()) _uiState.update { it.copy(isErrorDepartureCity = true) }
        if (_uiState.value.arrivalCityName.isEmpty()) _uiState.update { it.copy(isErrorArrivalCity = true) }

        if (!_uiState.value.isErrorArrivalCity && !_uiState.value.isErrorDepartureCity) {
            val item = TripListItemData(
                departureCity = _uiState.value.arrivalCityName,
                departureCountry = _uiState.value.arrivalCountryName,
                date = _uiState.value.date.let { it.convertMillisToDate(it) },
                departureAirport = _uiState.value.arrivalAirportName,
                arrivalCity = _uiState.value.departureCityName,
                arrivalCountry = _uiState.value.departureCountryName,
                arrivalAirport = _uiState.value.departureAirportName,
                departureCodeAirport = _uiState.value.departureIata,
                arrivalCodeAirport = _uiState.value.arrivalIata,
            )

            viewModelScope.launch {
                runCatching {
                    tripListRepository.saveDataTile(data = item.toItem())
                    _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnNextClicked)
                }
            }
        }
    }
}