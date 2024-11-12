package com.example.wherenow.ui.triplist.model

internal sealed class TripListNavigationEvent {
    data object OnChangeMode : TripListNavigationEvent()
    data object OnAddTrip : TripListNavigationEvent()
}