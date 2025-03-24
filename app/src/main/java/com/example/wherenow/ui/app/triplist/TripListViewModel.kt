package com.example.wherenow.ui.app.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetActuallyTripListUseCase
import com.example.wherenow.data.usecases.GetFutureTripListUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.app.triplist.model.TripListViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class TripListViewModel(
    private val deleteTileOnListUseCase: DeleteTileOnListUseCase,
    private val getPastTripListUseCase: GetPastTripListUseCase,
    private val getActuallyTripListUseCase: GetActuallyTripListUseCase,
    private val getFutureTripListUseCase: GetFutureTripListUseCase
) : ViewModel() {

    private val _navigationEvents = Channel<TripListNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripListViewState())
    val uiState: StateFlow<TripListViewState> = _uiState.asStateFlow()

    init {
        getList(uiState.value.selectedButtonType)
    }

    internal fun onUiIntent(uiIntent: TripListUiIntent) {
        when (uiIntent) {
            TripListUiIntent.OnAddTrip -> onAddTrip()
            TripListUiIntent.OnCloseApp -> onCloseApp()
            is TripListUiIntent.OnDeleteTrip -> onDeleteTrip(uiIntent.id, uiIntent.selectedButton)
            is TripListUiIntent.ShowTripDetails -> showTripDetails(uiIntent.tileId)
            is TripListUiIntent.OnGetListDependsButtonType -> getList(uiIntent.selectedButton)
        }
    }

    private fun showTripDetails(tileId: Int) = _navigationEvents.trySend(TripListNavigationEvent.OnShowDetailsTrip(tileId))

    private fun onCloseApp() = _navigationEvents.trySend(TripListNavigationEvent.OnCloseApp)

    private fun onAddTrip() = _navigationEvents.trySend(TripListNavigationEvent.OnAddTrip)

    private fun onDeleteTrip(id: Int, selectedButton: TripListDataEnum) {
        viewModelScope.launch {
            deleteTileOnListUseCase.invoke(id = id)
            getList(selectedButton)
        }
    }

    private fun getList(selectedButton: TripListDataEnum) =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    tripList = when (selectedButton) {
                        TripListDataEnum.PAST -> {
                            _uiState.update { update -> update.copy(selectedButtonType = TripListDataEnum.PAST) }
                            getPastTripListUseCase.invoke()
                        }

                        TripListDataEnum.PRESENT -> {
                            _uiState.update { update -> update.copy(selectedButtonType = TripListDataEnum.PRESENT) }
                            getActuallyTripListUseCase.invoke()
                        }

                        else -> {
                            _uiState.update { update -> update.copy(selectedButtonType = TripListDataEnum.FUTURE) }
                            getFutureTripListUseCase.invoke()
                        }
                    }
                )
            }
            _uiState.update { it.copy(isLoading = false) }
        }
}