package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.DeleteImportantNoteUseCase
import com.example.wherenow.data.usecases.GetImportantNotesListUseCase
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesUiIntent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesViewState
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
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
internal class ImportantNotesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getImportantNotesListUseCase: GetImportantNotesListUseCase,
    private val deleteImportantNoteUseCase: DeleteImportantNoteUseCase
) : ViewModel() {

    private val tripId: Int = checkNotNull(savedStateHandle[TripTileDetailsTag.TRIP_ID])

    private val _navigationEvents = Channel<ImportantNotesNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(ImportantNotesViewState())
    val uiState: StateFlow<ImportantNotesViewState> = _uiState.asStateFlow()

    init {
        getImportantNotesList()
        _uiState.update { it.copy(tripId = tripId) }
    }

    internal fun onUiIntent(uiIntent: ImportantNotesUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                ImportantNotesUiIntent.OnBackClicked -> _navigationEvents.trySend(ImportantNotesNavigationEvent.OnBack)
                is ImportantNotesUiIntent.OnAddNotes -> _navigationEvents.trySend(ImportantNotesNavigationEvent.OnAddNotes(uiIntent.tripId))
                is ImportantNotesUiIntent.OnDeleteNote -> onDeleteNote(uiIntent.id)
                is ImportantNotesUiIntent.OnEditNote -> onEditNote(uiIntent.note)
            }
        }
    }

    private fun getImportantNotesList() {
        viewModelScope.launch {
            runCatching {
                getImportantNotesListUseCase.invoke().let { getList ->
                    _uiState.update {
                        it.copy(
                            notesList = getList.first()
                                .filter { trip -> trip.tripId == tripId }
                        )
                    }
                }
            }
        }
    }

    private fun onDeleteNote(id: Int) {
        viewModelScope.launch {
            deleteImportantNoteUseCase.invoke(id = id)
            getImportantNotesList()
        }
    }

    private fun onEditNote(note: ImportantNoteItemData) {
        _navigationEvents.trySend(
            ImportantNotesNavigationEvent.OnEditNote(
                ImportantNoteItemData(
                    title = note.title,
                    description = note.description,
                    id = note.id,
                    tripId = note.tripId
                )
            )
        )
    }
}