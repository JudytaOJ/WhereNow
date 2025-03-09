package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesNavigationEvent

internal const val IMPORTANT_NOTES = "wherenow/ui/app/importantnotes"

internal fun NavController.navigateToImportantNotes() {
    navigate(IMPORTANT_NOTES)
}

internal fun NavGraphBuilder.importantNotes(
    onNavigateBack: () -> Unit,
    onAddNotes: () -> Unit,
    onEditNote: (ImportantNoteItemData) -> Unit
) {
    composable(
        route = IMPORTANT_NOTES
    ) {
        ImportantNotesScreen { events ->
            when (events) {
                ImportantNotesNavigationEvent.OnBack -> onNavigateBack()
                ImportantNotesNavigationEvent.OnAddNotes -> onAddNotes()
                is ImportantNotesNavigationEvent.OnEditNote -> onEditNote(events.note)
            }
        }
    }
}