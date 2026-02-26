package com.example.wherenow.ui.app.settingsmenu.flightStatistics.models

internal sealed class FlightStatisticsNavigationEvent {
    data object OnBackNavigation : FlightStatisticsNavigationEvent()
}