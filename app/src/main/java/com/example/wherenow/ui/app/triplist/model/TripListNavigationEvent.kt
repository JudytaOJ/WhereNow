package com.example.wherenow.ui.app.triplist.model

internal sealed class TripListNavigationEvent {
    data object OnCloseApp : TripListNavigationEvent()
    data object OnAddTrip : TripListNavigationEvent()
}