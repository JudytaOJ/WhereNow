package com.example.wherenow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.splashScreen.SplashScreen
import com.example.wherenow.ui.app.tripdatadetails.TripDataDetailsScreen
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.triplist.TripListScreen
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.util.navigateBack

@Composable
fun NavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController, startDestination = AppDestination.Splash.route
    ) {
        composable(AppDestination.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = AppDestination.ListTrip.route) {
            TripListScreen(
                navigationEvent = { events ->
                    when (events) {
                        TripListNavigationEvent.OnAddTrip -> navController.navigate(Screen.TRIP_DETAILS.name)
                        TripListNavigationEvent.OnChangeMode -> TODO()
                    }
                }
            )
        }
        composable(route = AppDestination.TripDetails.route) {
            TripDataDetailsScreen(
                navigationEvent = { events ->
                    when (events) {
                        TripDataDetailsNavigationEvent.OnBackNavigation -> navController.navigateBack()
                        is TripDataDetailsNavigationEvent.OnNextClicked -> navController.navigate(Screen.LIST_TRIP.name)
                    }
                }
            )
        }
    }
}