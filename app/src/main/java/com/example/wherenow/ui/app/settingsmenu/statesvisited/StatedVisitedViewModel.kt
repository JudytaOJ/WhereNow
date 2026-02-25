package com.example.wherenow.ui.app.settingsmenu.statesvisited

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.data.usecases.SaveStatesVisitedUseCase
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedViewState
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesProvider
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesVisitedUiIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class StatedVisitedViewModel(
    private val saveStatesVisitedUseCase: SaveStatesVisitedUseCase,
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase,
    private val statesProvider: StatesProvider
) : ViewModel() {

    private val _navigationEvents = Channel<StatedVisitedNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(StatedVisitedViewState())
    val uiState: StateFlow<StatedVisitedViewState> = _uiState.asStateFlow()

    internal fun onUiIntent(uiIntent: StatesVisitedUiIntent) {
        when (uiIntent) {
            StatesVisitedUiIntent.OnBackClicked -> _navigationEvents.trySend(StatedVisitedNavigationEvent.OnBackNavigation)
            is StatesVisitedUiIntent.OnCheckboxToggled -> onCheckboxToggled(uiIntent.id, uiIntent.isChecked)
            StatesVisitedUiIntent.MarkAnimationShown -> _uiState.update {
                it.copy(
                    hasShownAnimation = true,
                    showAnimation = false
                )
            }

            StatesVisitedUiIntent.ResetAnimation -> _uiState.update {
                it.copy(
                    hasShownAnimation = false,
                    showAnimation = true
                )
            }
        }
    }

    internal fun loadData() {
        viewModelScope.launch {
            val baseList = statesProvider.provide()
            val visitedList = getStatesVisitedUseCase.invoke(baseList)

            _uiState.update {
                it.copy(statesList = visitedList)
            }
        }
    }

    private fun onCheckboxToggled(id: Int, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedList = _uiState.value.statesList.map {
                if (it.id == id) it.copy(isChecked = isChecked) else it
            }
            _uiState.value = _uiState.value.copy(statesList = updatedList)
            saveStatesVisitedUseCase.invoke(updatedList)

            checkIfAllChecked()
        }
    }

    private fun checkIfAllChecked() {
        val allChecked = _uiState.value.statesList.all { it.isChecked }
        if (allChecked && !_uiState.value.hasShownAnimation) {
            _uiState.update {
                it.copy(
                    showAnimation = true
                )
            }
        }
    }
}