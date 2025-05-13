package com.example.wherenow.ui.app.settingsmenu.statesvisited

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent

internal const val STATES_VISITED = "wherenow/ui/app/settingsmenu_statesvisited"

internal fun NavController.navigateToStatesVisited() {
    navigate(STATES_VISITED)
}

internal fun NavGraphBuilder.statesVisited(
    onBackNavigation: () -> Unit
) {
    composable(
        route = STATES_VISITED
    ) {
        StatedVisitedScreen { events ->
            when (events) {
                StatedVisitedNavigationEvent.OnBackNavigation -> onBackNavigation()
            }
        }
    }
}