package com.example.wherenow.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wherenow.ui.app.error.errorScreen
import com.example.wherenow.ui.app.error.navigateToError
import com.example.wherenow.ui.app.splashScreen.SplashScreen
import com.example.wherenow.ui.app.tripdatadetails.navigateToTripDetails
import com.example.wherenow.ui.app.tripdatadetails.tripDetails
import com.example.wherenow.ui.app.triplist.navigateToTripList
import com.example.wherenow.ui.app.triplist.tripList
import com.example.wherenow.ui.app.triptiledetails.navigateToTripTile
import com.example.wherenow.ui.app.triptiledetails.tripTile
import com.example.wherenow.util.navigateBack

@Composable
fun NavHost(
    navController: NavHostController,
    onCloseApp: () -> Unit
) {
    Surface {
        NavHost(
            navController = navController,
            startDestination = AppDestination.Splash.route
        ) {
            composable(route = AppDestination.Splash.route) {
                SplashScreen(navController = navController)
            }
            tripList(
                onAddTrip = { navController.navigateToTripDetails() },
                onCloseApp = { onCloseApp() },
                onShowDetailsTrip = { tileId ->
                    navController.navigateToTripTile(tileId.toString())
                }
            )
            tripDetails(
                onBackNavigation = { navController.navigateBack() },
                onErrorNavigation = { navController.navigateToError() },
                onNextClicked = { navController.navigateToTripList() }
            )
            errorScreen(
                onClick = { navController.navigateToTripList() }
            )
            tripTile(
                onNavigateBack = { navController.navigateBack() }
            )
        }
    }
}