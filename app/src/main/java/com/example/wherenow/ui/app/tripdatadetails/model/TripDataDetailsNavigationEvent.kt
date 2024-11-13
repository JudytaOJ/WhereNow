package com.example.wherenow.ui.app.tripdatadetails.model

internal sealed class TripDataDetailsNavigationEvent {
    data object OnBackNavigation : TripDataDetailsNavigationEvent()
}