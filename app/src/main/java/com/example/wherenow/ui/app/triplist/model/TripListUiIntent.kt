package com.example.wherenow.ui.app.triplist.model

internal sealed class TripListUiIntent {
    data object OnChangeMode : TripListUiIntent()
    data object OnAddTrip : TripListUiIntent()
}