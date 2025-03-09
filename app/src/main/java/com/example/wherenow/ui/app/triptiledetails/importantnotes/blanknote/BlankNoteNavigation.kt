package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag

internal const val EDIT_TITLE = TripTileDetailsTag.TITLE_EDIT_NOTE
internal const val EDIT_DESCRIPTION = TripTileDetailsTag.DESCRIPTION_EDIT_NOTE
internal const val ID_NOTE = TripTileDetailsTag.ID_NOTE
internal const val TRIP_ID = TripTileDetailsTag.TRIP_ID
internal const val BLANK_NOTE_ROUTE = "wherenow/ui/app/importantnotes/blanknote?{$EDIT_TITLE}?{$EDIT_DESCRIPTION}?{$ID_NOTE}?{$TRIP_ID}"

internal fun NavController.navigateToBlankNote(title: String?, description: String?, id: String, tripId: String) {
    navigate(
        BLANK_NOTE_ROUTE.replace("{$EDIT_TITLE}", title.orEmpty())
            .replace("{$EDIT_DESCRIPTION}", description.orEmpty())
            .replace("{$ID_NOTE}", id)
            .replace("{$TRIP_ID}", tripId)
    )
}

internal fun NavGraphBuilder.blankNote(
    onNavigateBack: () -> Unit,
    addClickedEvent: (tripId: Int) -> Unit
) {
    composable(
        route = BLANK_NOTE_ROUTE,
        arguments = listOf(
            navArgument(EDIT_TITLE) {
                type = NavType.StringType
            },
            navArgument(EDIT_DESCRIPTION) {
                type = NavType.StringType
            },
            navArgument(ID_NOTE) {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(TRIP_ID) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        BlankNoteScreen { events ->
            when (events) {
                BlankNoteNavigationEvent.OnBackClicked -> onNavigateBack()
                is BlankNoteNavigationEvent.AddClickedEvent -> addClickedEvent(events.tripId)
            }
        }
    }
}