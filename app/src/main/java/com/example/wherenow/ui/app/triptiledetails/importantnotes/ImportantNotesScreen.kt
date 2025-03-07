package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.database.notes.Notes
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
            if (state.notesList.isNotEmpty()) {
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
            } else {
                ImportantNotesEmptyState()
            }
        }
    }
}

val SIZE_EMPTY_STATE = 350.dp

@Composable
private fun ImportantNotesEmptyState() {
    val emptyAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.important_notes_empty_state)
    )
    val emptyAnimationProgress by animateLottieCompositionAsState(
        composition = emptyAnimation,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.whereNowSpacing.space16),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = emptyAnimation,
            progress = emptyAnimationProgress,
            alignment = Alignment.BottomCenter,
            modifier = Modifier.size(SIZE_EMPTY_STATE)
        )
        Text(
            text = stringResource(R.string.important_notes_empty_state_text),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewLightDark
@Composable
private fun ImportantNotesPreview() {
    WhereNowTheme {
        ImportantNotes(
            state = ImportantNotesViewState(
                isLoading = false,
                notesList = listOf(
                    Notes(
                        title = "Title for preview",
                        description = LoremIpsum().values.joinToString(),
                        id = 1
                    ),
                    Notes(
                        title = "Title for preview",
                        description = LoremIpsum().values.joinToString(),
                        id = 2
                    )
                )
            ),
            uiIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun ImportantNotesEmptyStatePreview() {
    WhereNowTheme {
        ImportantNotesEmptyState()
    }
}