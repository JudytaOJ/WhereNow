package com.example.wherenow.ui.app.triptiledetails.filetile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag

internal const val TRIP_ID = TripTileDetailsTag.TRIP_ID
internal const val ADD_FILES = "wherenow/ui/app/filetile?{${TRIP_ID}}"

internal fun NavController.navigateToFile(tripId: String) {
    navigate(ADD_FILES.replace("{${TRIP_ID}}", tripId))
}

internal fun NavGraphBuilder.pdfViewer(
    onNavigateBack: () -> Unit,
    openFile: (FileData) -> Unit
) {
    composable(
        route = ADD_FILES,
        arguments = listOf(
            navArgument(TRIP_ID) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        FileScreen { events ->
            when (events) {
                FileNavigationEvent.OnBackClicked -> onNavigateBack()
                is FileNavigationEvent.OnOpenFile -> openFile(events.file)
            }
        }
    }
}