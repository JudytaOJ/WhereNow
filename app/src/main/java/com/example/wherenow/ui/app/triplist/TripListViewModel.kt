package com.example.wherenow.ui.app.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.app.triplist.model.TripListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
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
        getList()
    }

    internal fun onUiIntent(uiIntent: TripListUiIntent) {
        when (uiIntent) {
            TripListUiIntent.OnAddTrip -> onAddTrip()
            TripListUiIntent.OnCloseApp -> onCloseApp()
            is TripListUiIntent.OnDeleteTrip -> onDeleteTrip(uiIntent.id)
            is TripListUiIntent.ShowTripDetails -> _uiState.update { it.copy(showBottomSheet = true, detailsId = uiIntent.id) }
            TripListUiIntent.HideTripDetails -> _uiState.update { it.copy(showBottomSheet = false, detailsId = null) }
        }
    }

    private fun onCloseApp() = _navigationEvents.trySend(TripListNavigationEvent.OnCloseApp)

    private fun onAddTrip() = _navigationEvents.trySend(TripListNavigationEvent.OnAddTrip)

    private fun onDeleteTrip(id: Int) {
        viewModelScope.launch {
            repository.deletedDataTile(id = id)
            getList()
        }
    }

    private fun getList() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tripList = when (it.optionsList.first().name) {
                        TripListDataEnum.PAST.name -> {
                            repository.getListDataTile().first()
                                .filter { date -> date.date.takeLast(4) < LocalDate.now().year.toString() }
                                .sortedBy { sort -> sort.date }
                                .reversed()
                                .toImmutableList()
                        }

                        TripListDataEnum.PRESENT.name -> {
                            repository.getListDataTile().first()
                                .filter { date -> date.date.takeLast(4) == LocalDate.now().year.toString() }
                                .sortedBy { sort -> sort.date }
                                .reversed()
                                .toImmutableList()
                        }

                        else -> {
                            repository.getListDataTile().first()
                                .filter { date -> date.date.takeLast(4) > LocalDate.now().year.toString() }
                                .sortedBy { sort -> sort.date }
                                .reversed()
                                .toImmutableList()
                        }
                    }
                )
            }
        }
}