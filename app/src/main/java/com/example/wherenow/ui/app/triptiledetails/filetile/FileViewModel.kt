package com.example.wherenow.ui.app.triptiledetails.filetile

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.DeleteFileUseCase
import com.example.wherenow.data.usecases.GetFilesListUseCase
import com.example.wherenow.data.usecases.SaveFileUseCase
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent.OnOpenFile
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileUiIntent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileViewState
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FileViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFilesListUseCase: GetFilesListUseCase,
    private val saveFileUseCase: SaveFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase
) : ViewModel() {

    private val tripId: Int = savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) ?: 0

    private val _navigationEvents = Channel<FileNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(FileViewState())
    val uiState: StateFlow<FileViewState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(tripId = tripId) }
        observeFiles()
    }

    internal fun onUiIntent(uiIntent: FileUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                FileUiIntent.OnBackClicked -> _navigationEvents.trySend(FileNavigationEvent.OnBackClicked)
                is FileUiIntent.AddFile -> addFileClicked(uiIntent.file)
                is FileUiIntent.OpenFile -> _navigationEvents.trySend(OnOpenFile(uiIntent.id))
                is FileUiIntent.OnDeleteFile -> onDeleteFile(uiIntent.id)
            }
        }
    }

    private fun observeFiles() {
        getFilesListUseCase()
            .map { list ->
                list.filter { it.tripId == tripId }
                    .sortedBy { it.name }
            }.onEach { filtered ->
                _uiState.update {
                    it.copy(fileList = filtered)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun addFileClicked(file: FileData) {
        viewModelScope.launch {
            runCatching {
                saveFileUseCase.invoke(file.copy(tripId = tripId))
            }
        }
    }

    private fun onDeleteFile(file: Int) {
        viewModelScope.launch {
            deleteFileUseCase.invoke(id = file)
        }
    }

    internal fun getFileNameFromUri(context: Context, uri: Uri): String {
        var name = "plik.pdf"

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }

        return name
    }
}