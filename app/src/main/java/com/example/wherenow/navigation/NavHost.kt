package com.example.wherenow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.splashScreen.SplashScreen
import com.example.wherenow.ui.app.triplist.ListTripScreen
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent

@Composable
fun NavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash.route
    ) {
        composable(AppDestination.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = AppDestination.Home.route) {
            ListTripScreen(
                navigationEvent = { events ->
                    when (events) {
                        TripListNavigationEvent.OnAddTrip -> TODO()
                        TripListNavigationEvent.OnChangeMode -> TODO()
                    }
                }
            )
        }
    }
}