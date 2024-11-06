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
import javax.inject.Inject

@HiltViewModel
internal class WhereNowTripListViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvents = Channel<WhereNowTripListNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(WhereNowTripListViewState())
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