package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wherenow.R
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesUiIntent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesViewState
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowNotesTile
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

const val NAVIGATION_NOTES_KEY = "NavigationNotesKey"

@Composable
internal fun ImportantNotesScreen(
    navigationEvent: (ImportantNotesNavigationEvent) -> Unit
) {
    val viewModel: ImportantNotesViewModel = hiltViewModel()
    ImportantNotes(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_NOTES_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
internal fun ImportantNotes(
    state: ImportantNotesViewState,
    uiIntent: (ImportantNotesUiIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.trip_details_tile_list_name_important_notes),
                onBackAction = { uiIntent(ImportantNotesUiIntent.OnBackClicked) },
                isArrowVisible = true,
                isCloseAppIconVisible = false
            )
        },
        floatingActionButton = {
            WhereNowFloatingActionButton(
                onClick = { uiIntent(ImportantNotesUiIntent.OnAddNotes) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(MaterialTheme.whereNowSpacing.space16)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 120.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16)
            ) {
                items(state.notesList) {
                    WhereNowNotesTile(
                        titleNotes = state.notesList.find { it.title.isNotEmpty() }?.title.orEmpty(),
                        descriptionNotes = state.notesList.find { it.description.isNotEmpty() }?.description.orEmpty(),
                        onClick = {},
                        onDeleteClick = { uiIntent(ImportantNotesUiIntent.OnDeleteNote(it.id)) }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ImportantNotesPreview() {
    WhereNowTheme {
        ImportantNotes(
            state = ImportantNotesViewState(
                isLoading = false
            ),
            uiIntent = {}
        )
    }
}