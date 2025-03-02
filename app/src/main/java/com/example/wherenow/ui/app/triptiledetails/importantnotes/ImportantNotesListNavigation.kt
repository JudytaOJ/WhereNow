package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val IMPORTANT_NOTES = "wherenow/ui/app/importantnotes"

internal fun NavController.navigateToImportantNotes() {
    navigate(IMPORTANT_NOTES)
}

internal fun NavGraphBuilder.importantNotes(
) {
    composable(
        route = IMPORTANT_NOTES
    ) {
        ImportantNotes()
    }
}