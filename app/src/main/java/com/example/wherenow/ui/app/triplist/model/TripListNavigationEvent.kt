package com.example.wherenow.ui.app.triplist.model

internal sealed class TripListNavigationEvent {
    data object OnAddTrip : TripListNavigationEvent()
    data class OnShowDetailsTrip(val tileId: Int) : TripListNavigationEvent()

    //Menu app
    data object StatesVisited : TripListNavigationEvent()
    data object OnCloseApp : TripListNavigationEvent()
}