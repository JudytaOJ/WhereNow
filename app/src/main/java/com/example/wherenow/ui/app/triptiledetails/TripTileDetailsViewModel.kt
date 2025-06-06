package com.example.wherenow.ui.app.triptiledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent.AddFiles
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent.ImportantNotesDetails
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsUiIntent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class TripTileDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getListDataTileUseCase: GetListDataTileUseCase
) : ViewModel() {

    private val tripId: Int = checkNotNull(savedStateHandle[TripTileDetailsTag.TRIP_ID])

    private val _navigationEvents = Channel<TripTileDetailsNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripTileDetailsViewState())
    val uiState: StateFlow<TripTileDetailsViewState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        loadData()
        _uiState.update { it.copy(isLoading = false) }
    }

    internal fun onUiIntent(uiIntent: TripTileDetailsUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                TripTileDetailsUiIntent.OnBackClicked -> _navigationEvents.trySend(TripTileDetailsNavigationEvent.OnBack)
                is TripTileDetailsUiIntent.ShowTripDetails -> _uiState.update { it.copy(showBottomSheet = true, detailsId = tripId) }
                TripTileDetailsUiIntent.HideTripDetails -> _uiState.update { it.copy(showBottomSheet = false, detailsId = null) }
                is TripTileDetailsUiIntent.ImportantNotesDetails -> _navigationEvents.trySend(
                    ImportantNotesDetails(
                        uiIntent.tripId
                    )
                )

                is TripTileDetailsUiIntent.AddFiles -> _navigationEvents.trySend(
                    AddFiles(
                        uiIntent.tripId
                    )
                )
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatching {
                _uiState.update {
                    it.copy(
                        tripList = getListDataTileUseCase.invoke(),
                        detailsId = tripId
                    )
                }
            }
        }
    }
}