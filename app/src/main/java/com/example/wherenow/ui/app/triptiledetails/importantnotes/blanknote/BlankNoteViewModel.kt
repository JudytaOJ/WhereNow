package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.SaveImportantNoteUseCase
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteUiIntent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BlankNoteViewModel @Inject constructor(
    private val saveImportantNoteUseCase: SaveImportantNoteUseCase
) : ViewModel() {
    private val _navigationEvents = Channel<BlankNoteNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(BlankNoteViewState())
    val uiState: StateFlow<BlankNoteViewState> = _uiState.asStateFlow()

    internal fun onUiIntent(uiIntent: BlankNoteUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                BlankNoteUiIntent.OnBackClicked -> _navigationEvents.trySend(BlankNoteNavigationEvent.OnBackClicked)
                BlankNoteUiIntent.NextClickedAddNote -> addNoteClicked()
                is BlankNoteUiIntent.OnUpdateTitleNote -> onUpdateTitleNote(uiIntent.newValue)
                is BlankNoteUiIntent.OnUpdateDescriptionNote -> onUpdateDescriptionNote(uiIntent.newValue)
            }
        }
    }

    private fun onUpdateTitleNote(newValue: String) = _uiState.update { state -> state.copy(titleNote = newValue) }
    private fun onUpdateDescriptionNote(newValue: String) = _uiState.update { state -> state.copy(descriptionNote = newValue) }

    private fun addNoteClicked() {
        viewModelScope.launch {
            runCatching {
                saveImportantNoteUseCase.invoke(
                    ImportantNoteItemData(
                        title = _uiState.value.titleNote,
                        description = _uiState.value.descriptionNote
                    )
                )
                _navigationEvents.trySend(BlankNoteNavigationEvent.AddClickedEvent)
            }
        }
    }
}