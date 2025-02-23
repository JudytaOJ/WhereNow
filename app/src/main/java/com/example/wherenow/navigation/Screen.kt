package com.example.wherenow.navigation

enum class Screen {
    SPLASH
}

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination(Screen.SPLASH.name)
}