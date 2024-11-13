package com.example.wherenow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.splashScreen.SplashScreen
import com.example.wherenow.ui.app.tripdatadetails.TripDataDetailsScreen
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.triplist.ListTripScreen
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
        composable(route = AppDestination.Home.route) {
            ListTripScreen(navigationEvent = { events ->
                when (events) {
                    TripListNavigationEvent.OnAddTrip -> navController.navigate(Screen.TRIP_DATA.name)
                    TripListNavigationEvent.OnChangeMode -> TODO()
                }
            })
        }
        composable(route = AppDestination.TripData.route) {
            TripDataDetailsScreen(navigationEvent = { events ->
                when (events) {
                    TripDataDetailsNavigationEvent.OnBackNavigation -> navController.navigateBack()
                }
            })
        }
    }
}