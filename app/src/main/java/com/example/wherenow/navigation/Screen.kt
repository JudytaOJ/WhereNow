package com.example.wherenow.navigation

enum class Screen {
    SPLASH,
    LIST_TRIP,
    TRIP_DETAILS,
    ERROR_SCREEN
}

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination(Screen.SPLASH.name)
    data object ListTrip : AppDestination(Screen.LIST_TRIP.name)
    data object TripDetails : AppDestination(Screen.TRIP_DETAILS.name)
    data object ErrorScreen: AppDestination(Screen.ERROR_SCREEN.name)
}