package com.example.wherenow.ui.triplist.model

internal sealed class WhereNowTripListUiIntent {
    data object OnChangeMode : WhereNowTripListUiIntent()
    data object OnAddTrip : WhereNowTripListUiIntent()
}