package com.example.wherenow.ui.app.tripdatadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.dto.DistanceAirportDto
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.data.usecases.GetCityListFromRepositoryUseCase
import com.example.wherenow.data.usecases.GetDistanceBetweenAirportUseCase
import com.example.wherenow.data.usecases.SaveCityListUseCase
import com.example.wherenow.data.usecases.SaveDataTileUseCase
import com.example.wherenow.data.usecases.SendPushUseCase
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsViewState
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.util.StringUtils
import com.example.wherenow.util.textWithFirstUppercaseChar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.math.roundToInt

internal class TripDataDetailsViewModel(
    private val getAirportUseCase: GetAirportUseCase,
    private val saveDataTileUseCase: SaveDataTileUseCase,
    private val saveCityListUseCase: SaveCityListUseCase,
    private val getCityListFromRepositoryUseCase: GetCityListFromRepositoryUseCase,
    private val getDistanceBetweenAirport: GetDistanceBetweenAirportUseCase,
    private val sendPushUseCase: SendPushUseCase
) : ViewModel() {

    private val _navigationEvents = Channel<TripDataDetailsNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripDataDetailsViewState())
    val uiState: StateFlow<TripDataDetailsViewState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                val localDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

                if (getCityListFromRepositoryUseCase.invoke().isEmpty()) {
                    var i = 88
                    val maxPage = 109
                    while (i <= maxPage) {
                        getAirportUseCase(i).let {
                            saveCityListUseCase.invoke(it.find { find -> find?.airportList?.isNotEmpty() == true }?.airportList ?: emptyList())

                            _uiState.update { state ->
                                state.copy(
                                    cityList = getCityListFromRepositoryUseCase.invoke()
                                        .filter { filter -> filter.attributes.country == UNITED_STATES }
                                        .sortedBy { sort -> sort.attributes.city }
                                        .distinctBy { distinct -> distinct.attributes.city },
                                    date = localDate
                                )
                            }
                        }
                        i++
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            cityList = getCityListFromRepositoryUseCase.invoke()
                                .filter { filter -> filter.attributes.country == UNITED_STATES }
                                .sortedBy { sort -> sort.attributes.city }
                                .distinctBy { distinct -> distinct.attributes.city },
                            date = localDate
                        )
                    }
                }
            }.onFailure {
                it.printStackTrace()
                _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnErrorScreen)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
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
            is TripDataDetailsUiIntent.OnUpdateDate -> updateDate(uiIntent.newValue)

            TripDataDetailsUiIntent.HideModalFromCityList -> _uiState.update { it.copy(showBottomSheetFromCityList = false) }
            TripDataDetailsUiIntent.ShowModalFromCityList -> _uiState.update { it.copy(showBottomSheetFromCityList = true) }
            TripDataDetailsUiIntent.HideModalToCityList -> _uiState.update { it.copy(showBottomSheetToCityList = false) }
            TripDataDetailsUiIntent.ShowModalToCityList -> _uiState.update { it.copy(showBottomSheetToCityList = true) }
            is TripDataDetailsUiIntent.OnUpdateFromSearchText -> _uiState.update {
                it.copy(searchTextFrom = uiIntent.text.textWithFirstUppercaseChar())
            }

            is TripDataDetailsUiIntent.OnUpdateToSearchText -> _uiState.update {
                it.copy(searchTextTo = uiIntent.text.textWithFirstUppercaseChar())
            }

            TripDataDetailsUiIntent.OnClearFromSearchText -> _uiState.update { it.copy(searchTextFrom = StringUtils.EMPTY) }
            TripDataDetailsUiIntent.OnClearToSearchText -> _uiState.update { it.copy(searchTextTo = StringUtils.EMPTY) }
        }
    }

    private fun updateDepartureCity(newValue: String) {
        _uiState.update { state -> state.copy(arrivalCity = newValue, isErrorDepartureCity = false) }
        if (_uiState.value.departureCity.isNotEmpty() && _uiState.value.arrivalCity.isNotEmpty()) {
            getDistanceInMiles()
        }
    }

    private fun updateDepartureAirportCode(newValue: String) = _uiState.update { state -> state.copy(arrivalCodeAirport = newValue) }
    private fun updateDepartureCountry(newValue: String) = _uiState.update { state -> state.copy(arrivalCountry = newValue) }
    private fun updateDepartureAirportName(newValue: String) = _uiState.update { state -> state.copy(arrivalAirport = newValue) }
    private fun updateArrivalCity(newValue: String) {
        _uiState.update { state -> state.copy(departureCity = newValue, isErrorArrivalCity = false) }
        if (_uiState.value.departureCity.isNotEmpty() && _uiState.value.arrivalCity.isNotEmpty()) {
            getDistanceInMiles()
        }
    }

    private fun updateArrivalAirportCode(newValue: String) = _uiState.update { state -> state.copy(departureCodeAirport = newValue) }
    private fun updateArrivalCountry(newValue: String) = _uiState.update { state -> state.copy(departureCountry = newValue) }
    private fun updateArrivalAirportName(newValue: String) = _uiState.update { state -> state.copy(departureAirport = newValue) }
    private fun updateDate(newValue: Long) = _uiState.update { state -> state.copy(date = newValue) }

    private fun getDistanceInMiles() {
        viewModelScope.launch(Dispatchers.Main) {
            getDistanceBetweenAirport.invoke(
                from = DistanceAirportDto(
                    from = _uiState.value.departureCodeAirport,
                    to = _uiState.value.arrivalCodeAirport
                )
            ).let { distanceAirport ->
                _uiState.update { state ->
                    state.copy(
                        distance = distanceAirport.first()?.distanceAirportList?.attributes?.miles?.toDouble()?.roundToInt().toString()
                    )
                }
            }
        }
    }

    private fun onNextClicked() {
        if (_uiState.value.arrivalCity.isEmpty()) _uiState.update { it.copy(isErrorDepartureCity = true) }
        if (_uiState.value.departureCity.isEmpty()) _uiState.update { it.copy(isErrorArrivalCity = true) }

        if (!_uiState.value.isErrorArrivalCity && !_uiState.value.isErrorDepartureCity) {
            val item = TripListItemData(
                departureCity = _uiState.value.departureCity,
                departureCountry = _uiState.value.departureCountry,
                date = _uiState.value.date,
                image = _uiState.value.image.let { WhereNowDetailsTileImageType.entries.random().icon },
                departureAirport = _uiState.value.departureAirport,
                departureCodeAirport = _uiState.value.departureCodeAirport,
                arrivalCity = _uiState.value.arrivalCity,
                arrivalCountry = _uiState.value.arrivalCountry,
                arrivalAirport = _uiState.value.arrivalAirport,
                arrivalCodeAirport = _uiState.value.arrivalCodeAirport,
                distance = _uiState.value.distance,
                id = _uiState.value.id
            )

            viewModelScope.launch {
                runCatching {
                    val tripId = saveDataTileUseCase.invoke(trip = item)
                    sendPushUseCase.invoke(
                        id = tripId,
                        date = item.date
                    )
                    _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnNextClicked)
                }
            }
        }
    }
}

const val UNITED_STATES = "United States"