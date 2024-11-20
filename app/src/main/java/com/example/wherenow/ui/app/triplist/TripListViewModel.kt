package com.example.wherenow.ui.app.triplist

import androidx.lifecycle.ViewModel
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class TripListViewModel @Inject constructor(
    private val repository: TripListRepository
) : ViewModel() {

    private val _navigationEvents =
        Channel<TripListNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(TripListViewState())
    val uiState: StateFlow<TripListViewState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                tripList = listOf(
                    TripListItemData(
                        city = repository.getDataTile()?.city.orEmpty(),
                        country = repository.getDataTile()?.country.orEmpty(),
                        date = repository.getDataTile()?.date.orEmpty()
                    )
                ),
                cityName = repository.getDataTile()?.city.orEmpty(),
                countryName = repository.getDataTile()?.country.orEmpty(),
                date = repository.getDataTile()?.date.orEmpty()
            )
        }
    }

    internal fun onUiIntent(uiIntent: TripListUiIntent) {
        when (uiIntent) {
            TripListUiIntent.OnAddTrip -> onAddTrip()
            TripListUiIntent.OnChangeMode -> onChangeMode()
        }
    }

    private fun onChangeMode() {
        _navigationEvents.trySend(TripListNavigationEvent.OnChangeMode)
    }

    private fun onAddTrip() {
        _navigationEvents.trySend(TripListNavigationEvent.OnAddTrip)
    }
}