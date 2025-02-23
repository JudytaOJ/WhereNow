package com.example.wherenow.ui.app.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetListDataTileUseCase
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
    private val getListDataTileUseCase: GetListDataTileUseCase,
    private val deleteTileOnListUseCase: DeleteTileOnListUseCase
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
                            getListDataTileUseCase.invoke().first()
                                .filter { date -> date.date.takeLast(4) < LocalDate.now().year.toString() }
                                .sortedBy { sort -> sort.date }
                                .reversed()
                                .toImmutableList()
                        }

                        TripListDataEnum.PRESENT -> {
                            _uiState.update { update -> update.copy(selectedButtonType = TripListDataEnum.PRESENT) }
                            getListDataTileUseCase.invoke().first()
                                .filter { date -> date.date.takeLast(4) == LocalDate.now().year.toString() }
                                .sortedBy { sort -> sort.date }
                                .reversed()
                                .toImmutableList()
                        }

                        else -> {
                            _uiState.update { update -> update.copy(selectedButtonType = TripListDataEnum.FUTURE) }
                            getListDataTileUseCase.invoke().first()
                                .filter { date -> date.date.takeLast(4) > LocalDate.now().year.toString() }
                                .sortedBy { sort -> sort.date }
                                .reversed()
                                .toImmutableList()
                        }
                    }
                )
            }
            _uiState.update { it.copy(isLoading = false) }
        }
}