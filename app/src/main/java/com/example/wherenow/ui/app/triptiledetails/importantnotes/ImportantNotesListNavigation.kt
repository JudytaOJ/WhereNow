package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag

internal const val TRIP_ID = TripTileDetailsTag.TRIP_ID
internal const val IMPORTANT_NOTES = "wherenow/ui/app/importantnotes?{${TRIP_ID}}"

internal fun NavController.navigateToImportantNotes(tripId: String) {
    navigate(IMPORTANT_NOTES.replace("{${TRIP_ID}}", tripId))
}

internal fun NavGraphBuilder.importantNotes(
    onNavigateBack: () -> Unit,
    onAddNotes: (tripId: Int) -> Unit,
    onEditNote: (ImportantNoteItemData) -> Unit
) {
    composable(
        route = IMPORTANT_NOTES,
        arguments = listOf(
            navArgument(TRIP_ID) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        ImportantNotesScreen { events ->
            when (events) {
                ImportantNotesNavigationEvent.OnBack -> onNavigateBack()
                is ImportantNotesNavigationEvent.OnAddNotes -> onAddNotes(events.tripId)
                is ImportantNotesNavigationEvent.OnEditNote -> onEditNote(events.note)
            }
        }
    }
}