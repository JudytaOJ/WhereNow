package com.example.wherenow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wherenow.ui.splashScreen.SplashScreen
import com.example.wherenow.ui.triplist.WhereNowListTrip
import com.example.wherenow.ui.triplist.model.WhereNowTripListNavigationEvent

@Composable
fun WhereNowNavHost(
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
            WhereNowListTrip(
                navigationEvent = { events ->
                    when (events) {
                        WhereNowTripListNavigationEvent.OnAddTrip -> TODO()
                        WhereNowTripListNavigationEvent.OnChangeMode -> TODO()
                    }
                }
            )
        }
    }
}