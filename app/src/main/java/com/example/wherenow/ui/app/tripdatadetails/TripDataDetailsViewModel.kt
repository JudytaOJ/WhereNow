package com.example.wherenow.ui.app.tripdatadetails

import androidx.lifecycle.ViewModel
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsUiIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

internal class TripDataDetailsViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvents =
        Channel<TripDataDetailsNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripDataDetailsViewState())
    val uiState: StateFlow<TripDataDetailsViewState> = _uiState.asStateFlow()

    internal fun onUiIntent(uiIntent: TripDataDetailsUiIntent) {
        when (uiIntent) {
            TripDataDetailsUiIntent.OnBackNavigation -> _navigationEvents.trySend(TripDataDetailsNavigationEvent.OnBackNavigation)
        }
    }
}