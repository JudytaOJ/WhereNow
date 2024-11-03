package com.example.wherenow.navigation

enum class Screen {
    SPLASH,
    HOME
}

sealed class NavigationItem(val route: String) {
    data object Splash : NavigationItem(Screen.SPLASH.name)
    data object Home : NavigationItem(Screen.HOME.name)
}