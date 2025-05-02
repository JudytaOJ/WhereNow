package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.wherenow.R
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.NAVIGATION_NOTES_KEY
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteUiIntent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteViewState
import com.example.wherenow.ui.components.WhereNowButton
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.components.textfield.WhereNowEditableTextField
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun BlankNoteScreen(
    navigationEvent: (BlankNoteNavigationEvent) -> Unit
) {
    val viewModel: BlankNoteViewModel = koinViewModel()
    BlankNoteContentScreen(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_NOTES_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun BlankNoteContentScreen(
    state: BlankNoteViewState,
    uiIntent: (BlankNoteUiIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.trip_details_tile_list_name_important_notes),
                onBackAction = { uiIntent(BlankNoteUiIntent.OnBackClicked) },
                isArrowVisible = true,
                isCloseAppIconVisible = false
            )
        },
        bottomBar = {
            WhereNowButton(
                modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space16),
                buttonText = if (state.id != 0) stringResource(R.string.button_text_edit) else stringResource(R.string.button_text_add),
                onClick = {
                    uiIntent(
                        BlankNoteUiIntent.NextClickedAddOrEditNote(
                            ImportantNoteItemData(
                                title = state.titleNote,
                                description = state.descriptionNote,
                                id = state.id,
                                tripId = state.tripId
                            )
                        )
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
            ) {
                WhereNowEditableTextField(
                    text = state.titleNote,
                    placeholder = stringResource(R.string.blank_note_title),
                    maxLines = 2,
                    onChangeValue = { uiIntent(BlankNoteUiIntent.OnUpdateTitleNote(it)) },
                    contentDescriptionAccessibility = state.titleNote
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space8),
                    color = MaterialTheme.colorScheme.onBackground
                )
                WhereNowEditableTextField(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    text = state.descriptionNote,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = stringResource(R.string.blank_note_description),
                    onChangeValue = { uiIntent(BlankNoteUiIntent.OnUpdateDescriptionNote(it)) },
                    contentDescriptionAccessibility = state.descriptionNote
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BlankNoteContentScreenPreview() {
    WhereNowTheme {
        BlankNoteContentScreen(
            state = BlankNoteViewState(
                titleNote = "What not to forget",
                descriptionNote = LoremIpsum().values.joinToString()
            ),
            uiIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun BlankNoteShortDescriptionPreview() {
    WhereNowTheme {
        BlankNoteContentScreen(
            state = BlankNoteViewState(
                titleNote = "What not to forget",
                descriptionNote = "What not to forget"
            ),
            uiIntent = {}
        )
    }
}