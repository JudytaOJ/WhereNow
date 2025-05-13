package com.example.wherenow.ui.app.triplist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent

internal const val TRIP_LIST_ROUTE = "wherenow/ui/app/triplist"

internal fun NavController.navigateToTripList() {
    navigate(TRIP_LIST_ROUTE)
}

internal fun NavGraphBuilder.tripList(
    onAddTrip: () -> Unit,
    onCloseApp: () -> Unit,
    onStatesVisited: () -> Unit,
    onShowDetailsTrip: (tileId: Int) -> Unit
) {
    composable(
        route = TRIP_LIST_ROUTE
    ) {
        TripListScreen { events ->
            when (events) {
                TripListNavigationEvent.OnAddTrip -> onAddTrip()
                TripListNavigationEvent.OnCloseApp -> onCloseApp()
                is TripListNavigationEvent.OnShowDetailsTrip -> onShowDetailsTrip(events.tileId)
                TripListNavigationEvent.StatesVisited -> onStatesVisited()
            }
        }
    }
}