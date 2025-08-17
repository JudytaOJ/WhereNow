package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.SaveImportantNoteUseCase
import com.example.wherenow.data.usecases.UpdateImportantNoteUseCase
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteUiIntent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteViewState
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal class BlankNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val saveImportantNoteUseCase: SaveImportantNoteUseCase,
    private val updateImportantNoteUseCase: UpdateImportantNoteUseCase
) : ViewModel() {

    private val title: String = checkNotNull(savedStateHandle[TripTileDetailsTag.TITLE_EDIT_NOTE])
    private val description: String = checkNotNull(savedStateHandle[TripTileDetailsTag.DESCRIPTION_EDIT_NOTE])
    private val id: Int = checkNotNull(savedStateHandle[TripTileDetailsTag.ID_NOTE])
    private val tripId: Int = checkNotNull(savedStateHandle[TripTileDetailsTag.TRIP_ID])

    private val _navigationEvents = Channel<BlankNoteNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(BlankNoteViewState())
    val uiState: StateFlow<BlankNoteViewState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                titleNote = title,
                descriptionNote = description,
                id = id,
                tripId = tripId
            )
        }
    }

    internal fun onUiIntent(uiIntent: BlankNoteUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                BlankNoteUiIntent.OnBackClicked -> _navigationEvents.trySend(BlankNoteNavigationEvent.OnBackClicked)
                is BlankNoteUiIntent.NextClickedAddOrEditNote -> addOrEditNoteClicked(uiIntent.note)
                is BlankNoteUiIntent.OnUpdateTitleNote -> onUpdateTitleNote(uiIntent.newValue)
                is BlankNoteUiIntent.OnUpdateDescriptionNote -> onUpdateDescriptionNote(uiIntent.newValue)
            }
        }
    }

    private fun onUpdateTitleNote(newValue: String) = _uiState.update { state -> state.copy(titleNote = newValue) }
    private fun onUpdateDescriptionNote(newValue: String) = _uiState.update { state -> state.copy(descriptionNote = newValue) }

    private fun addOrEditNoteClicked(note: ImportantNoteItemData) {
        viewModelScope.launch {
            runCatching {
                if (note.id == id) {
                    updateImportantNoteUseCase.invoke(note = note).let {
                        _uiState.update {
                            it.copy(
                                titleNote = note.title,
                                descriptionNote = note.description
                            )
                        }
                    }
                }
                saveImportantNoteUseCase.invoke(note)
            }
            _navigationEvents.trySend(BlankNoteNavigationEvent.AddClickedEvent(tripId))
        }
    }
}