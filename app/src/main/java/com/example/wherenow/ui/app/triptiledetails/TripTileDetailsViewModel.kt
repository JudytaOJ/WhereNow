package com.example.wherenow.ui.app.triptiledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.R
import com.example.wherenow.data.usecases.AddCalendarFlightUseCase
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.data.usecases.IsTripAddedToCalendarUseCase
import com.example.wherenow.data.usecases.ObserveTripCalendarStatusUseCase
import com.example.wherenow.data.usecases.SaveTripAddedToCalendarUseCase
import com.example.wherenow.repository.calendar.models.CalendarFlightModel
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent.AddFiles
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent.ImportantNotesDetails
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsUiIntent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsViewState
import com.example.wherenow.util.ResourceProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

internal class TripTileDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getListDataTileUseCase: GetListDataTileUseCase,
    private val addCalendarEventUseCase: AddCalendarFlightUseCase,
    private val saveTripAddedToCalendarUseCase: SaveTripAddedToCalendarUseCase,
    private val observeTripCalendarStatusUseCase: ObserveTripCalendarStatusUseCase,
    private val isTripAddedToCalendarUseCase: IsTripAddedToCalendarUseCase,
    private val stringProvider: ResourceProvider
) : ViewModel() {

    private val tripId: Int = checkNotNull(savedStateHandle[TripTileDetailsTag.TRIP_ID])

    private val _navigationEvents = MutableSharedFlow<TripTileDetailsNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _uiState = MutableStateFlow(TripTileDetailsViewState())
    val uiState: StateFlow<TripTileDetailsViewState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        observeCalendarStatus()
        loadData()
        _uiState.update { it.copy(isLoading = false) }
    }

    internal fun onUiIntent(uiIntent: TripTileDetailsUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                TripTileDetailsUiIntent.OnBackClicked -> _navigationEvents.tryEmit(TripTileDetailsNavigationEvent.OnBack)
                is TripTileDetailsUiIntent.ShowTripDetails -> _uiState.update { it.copy(showBottomSheet = true, detailsId = tripId) }
                TripTileDetailsUiIntent.HideTripDetails -> _uiState.update { it.copy(showBottomSheet = false, detailsId = null) }
                is TripTileDetailsUiIntent.ImportantNotesDetails -> _navigationEvents.tryEmit(ImportantNotesDetails(tripId = uiIntent.tripId))
                is TripTileDetailsUiIntent.AddFiles -> _navigationEvents.tryEmit(AddFiles(tripId = uiIntent.tripId))
                is TripTileDetailsUiIntent.AddTripToCalendar -> _navigationEvents.tryEmit(TripTileDetailsNavigationEvent.RequestCalendarPermissions)
                is TripTileDetailsUiIntent.PermissionsResult -> permissionResult(granted = uiIntent.granted)
            }
        }
    }

    private fun permissionResult(granted: Boolean) {
        viewModelScope.launch {
            if (granted) {
                val trip = _uiState.value.tripList.find { it.id == tripId }
                if (trip != null) {
                    val zone = ZoneId.systemDefault()
                    val tripInstant = Instant.ofEpochMilli(trip.date)
                    val tripDate = tripInstant.atZone(zone).toLocalDate()

                    val startTime = tripDate.atTime(0, 1).atZone(zone).toInstant().toEpochMilli()
                    val endTime = tripDate.atTime(23, 59).atZone(zone).toInstant().toEpochMilli()

                    val model = CalendarFlightModel(
                        title = stringProvider.getString(R.string.calendar_app_destination, trip.departureCity),
                        description = stringProvider.getString(R.string.calendar_app_all_trip_name, trip.arrivalCity, trip.departureCity),
                        startTimeMillis = startTime,
                        endTimeMillis = endTime
                    )

                    val success = addCalendarEventUseCase.invoke(model)
                    saveTripAddedToCalendarUseCase(tripId, true)
                    if (success) {
                        if (_uiState.value.isTripAddedToCalendar) {
                            _navigationEvents.emit(TripTileDetailsNavigationEvent.NavigateToCalendarApp(startTimeMillis = startTime))
                        } else {
                            _navigationEvents.tryEmit(TripTileDetailsNavigationEvent.ShowEventAddedMessage)
                        }
                    } else _navigationEvents.tryEmit(TripTileDetailsNavigationEvent.ShowEventAddFailedMessage)
                } else {
                    _navigationEvents.tryEmit(TripTileDetailsNavigationEvent.ShowEventAddFailedMessage)
                }
            } else {
                _navigationEvents.tryEmit(TripTileDetailsNavigationEvent.ShowCalendarPermissionDeniedMessage)
            }
        }
    }

    private fun observeCalendarStatus() {
        viewModelScope.launch {
            observeTripCalendarStatusUseCase(tripId).collect { isAdded ->
                _uiState.update { it.copy(isTripAddedToCalendar = isAdded) }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatching {
                _uiState.update {
                    it.copy(
                        tripList = getListDataTileUseCase.invoke(),
                        detailsId = tripId,
                        isTripAddedToCalendar = isTripAddedToCalendarUseCase.invoke(tripId)
                    )
                }
            }
        }
    }
}