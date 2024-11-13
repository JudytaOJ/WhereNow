package com.example.wherenow.navigation

enum class Screen {
    SPLASH,
    HOME,
    TRIP_DATA
}

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination(Screen.SPLASH.name)
    data object Home : AppDestination(Screen.HOME.name)
    data object TripData : AppDestination(Screen.TRIP_DATA.name)
}