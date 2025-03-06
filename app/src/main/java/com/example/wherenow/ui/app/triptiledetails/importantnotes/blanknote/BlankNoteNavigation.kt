package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent

internal const val BLANK_NOTE = "wherenow/ui/app/importantnotes/blanknote"

internal fun NavController.navigateToBlankNote() {
    navigate(BLANK_NOTE)
}

internal fun NavGraphBuilder.blankNote(
    onNavigateBack: () -> Unit,
    addClickedEvent: () -> Unit
) {
    composable(
        route = BLANK_NOTE
    ) {
        BlankNoteScreen { events ->
            when (events) {
                BlankNoteNavigationEvent.OnBackClicked -> onNavigateBack()
                is BlankNoteNavigationEvent.AddClickedEvent -> addClickedEvent()
            }
        }
    }
}