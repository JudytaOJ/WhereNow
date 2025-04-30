package com.example.wherenow.ui.app.triptiledetails.filetile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.DeleteFileUseCase
import com.example.wherenow.data.usecases.GetFilesListUseCase
import com.example.wherenow.data.usecases.SaveFileUseCase
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileUiIntent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileViewState
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FileViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFilesListUseCase: GetFilesListUseCase,
    private val saveFileUseCase: SaveFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase
) : ViewModel() {

    private val tripId: Int = checkNotNull(savedStateHandle[TripTileDetailsTag.TRIP_ID])

    private val _navigationEvents = Channel<FileNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(FileViewState())
    val uiState: StateFlow<FileViewState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(tripId = tripId) }
        getFilesList()
    }

    internal fun onUiIntent(uiIntent: FileUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                FileUiIntent.OnBackClicked -> _navigationEvents.trySend(FileNavigationEvent.OnBackClicked)
                is FileUiIntent.AddFile -> addFileClicked(uiIntent.file)
                is FileUiIntent.OpenFile -> _navigationEvents.trySend(FileNavigationEvent.OnOpenFile(uiIntent.id))
                is FileUiIntent.OnDeleteFile -> onDeleteFile(uiIntent.id)
            }
        }
    }

    private fun getFilesList() {
        viewModelScope.launch {
            runCatching {
                getFilesListUseCase.invoke().let { list ->
                    _uiState.update {
                        it.copy(
                            fileList = list
//                                .filter { it.tripId == tripId }
                                .sortedBy { it.name }
                        )
                    }
                }
            }
        }
    }

    private fun addFileClicked(file: FileData) {
        viewModelScope.launch {
            runCatching {
                saveFileUseCase.invoke(file)
                getFilesList()
            }
        }
    }

    private fun onDeleteFile(file: Int) {
        viewModelScope.launch {
            deleteFileUseCase.invoke(id = file)
            getFilesList()
        }
    }
}