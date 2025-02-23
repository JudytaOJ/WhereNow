package com.example.wherenow.ui.app.tripdatadetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent


internal const val TRIP_DETAILS = "wherenow/ui/app/tripdetails"

internal fun NavController.navigateToTripDetails() {
    navigate(TRIP_DETAILS)
}

internal fun NavGraphBuilder.tripDetails(
    onBackNavigation: () -> Unit,
    onErrorNavigation: () -> Unit,
    onNextClicked: () -> Unit
) {
    composable(
        route = TRIP_DETAILS
    ) {
        TripDataDetailsScreen { events ->
            when (events) {
                TripDataDetailsNavigationEvent.OnBackNavigation -> onBackNavigation()
                TripDataDetailsNavigationEvent.OnErrorScreen -> onErrorNavigation()
                TripDataDetailsNavigationEvent.OnNextClicked -> onNextClicked()
            }
        }
    }
}