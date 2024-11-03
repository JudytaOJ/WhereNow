package com.example.wherenow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wherenow.ui.WhereNowTripList
import com.example.wherenow.ui.splashScreen.SplashScreen

@Composable
fun WhereNowNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Splash.route
    ) {
        composable(NavigationItem.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = NavigationItem.Home.route) {
            WhereNowTripList(navController)
        }
    }
}