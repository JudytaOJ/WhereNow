package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wherenow.R
import com.example.wherenow.ui.app.triptiledetails.importantnotes.NAVIGATION_NOTES_KEY
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteUiIntent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteViewState
import com.example.wherenow.ui.components.WhereNowButton
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.components.textfield.WhereNowEditableTextField
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

@Composable
internal fun BlankNoteScreen(
    navigationEvent: (BlankNoteNavigationEvent) -> Unit
) {
    val viewModel: BlankNoteViewModel = hiltViewModel()
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
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
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
                buttonText = stringResource(R.string.button_text),
                onClick = { uiIntent(BlankNoteUiIntent.NextClickedAddNote) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                .verticalScroll(rememberScrollState())
        ) {
            WhereNowEditableTextField(
                text = state.titleNote,
                placeholder = stringResource(R.string.blank_note_title),
                maxLines = 2,
                onChangeValue = { uiIntent(BlankNoteUiIntent.OnUpdateTitleNote(state.titleNote)) }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
            WhereNowEditableTextField(
                text = state.descriptionNote,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = stringResource(R.string.blank_note_description),
                onChangeValue = { uiIntent(BlankNoteUiIntent.OnUpdateDescriptionNote(state.descriptionNote)) }
            )
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