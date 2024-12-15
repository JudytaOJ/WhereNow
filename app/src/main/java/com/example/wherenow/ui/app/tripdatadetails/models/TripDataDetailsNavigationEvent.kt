package com.example.wherenow.ui.app.tripdatadetails.models

internal sealed class TripDataDetailsNavigationEvent {
    data object OnBackNavigation : TripDataDetailsNavigationEvent()
    data object OnNextClicked : TripDataDetailsNavigationEvent()
    data object OnErrorScreen : TripDataDetailsNavigationEvent()
}