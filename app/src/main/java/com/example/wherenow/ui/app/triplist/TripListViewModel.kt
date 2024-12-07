package com.example.wherenow.ui.app.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.app.triplist.model.TripListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TripListViewModel @Inject constructor(
    private val repository: TripListRepository
) : ViewModel() {

    private val _navigationEvents = Channel<TripListNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripListViewState())
    val uiState: StateFlow<TripListViewState> = _uiState.asStateFlow()

    init {
        loadData()
        refreshList()
    }

    internal fun onUiIntent(uiIntent: TripListUiIntent) {
        when (uiIntent) {
            TripListUiIntent.OnAddTrip -> onAddTrip()
            TripListUiIntent.OnChangeMode -> onChangeMode()
            is TripListUiIntent.OnDeleteTrip -> onDeleteTrip(uiIntent.id)
            is TripListUiIntent.ShowTripDetails -> _uiState.update { it.copy(showBottomSheet = true, detailsId = uiIntent.id) }
            TripListUiIntent.HideTripDetails -> _uiState.update { it.copy(showBottomSheet = false, detailsId = null) }
        }
    }

    private fun loadData() {
        viewModelScope.launch { _uiState.update { it.copy(tripList = repository.getListDataTile().first()) } }
    }

    private fun onChangeMode() = _navigationEvents.trySend(TripListNavigationEvent.OnChangeMode)

    private fun onAddTrip() = _navigationEvents.trySend(TripListNavigationEvent.OnAddTrip)

    private fun onDeleteTrip(id: Int) {
        viewModelScope.launch {
            repository.deletedDataTile(id = id)
            refreshList()
        }
    }

    private fun refreshList() = viewModelScope.launch { _uiState.update { it.copy(tripList = repository.getListDataTile().first()) } }
}