package com.example.wherenow.navigation

enum class Screen {
    SPLASH,
    HOME
}

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination(Screen.SPLASH.name)
    data object Home : AppDestination(Screen.HOME.name)
}