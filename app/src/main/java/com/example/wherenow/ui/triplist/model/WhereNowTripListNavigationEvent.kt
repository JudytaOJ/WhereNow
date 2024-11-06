package com.example.wherenow.ui.triplist.model

internal sealed class WhereNowTripListNavigationEvent {
    data object OnChangeMode : WhereNowTripListNavigationEvent()
    data object OnAddTrip : WhereNowTripListNavigationEvent()
}