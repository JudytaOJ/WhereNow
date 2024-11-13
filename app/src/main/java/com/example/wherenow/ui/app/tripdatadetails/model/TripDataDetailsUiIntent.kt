package com.example.wherenow.ui.app.tripdatadetails.model

internal sealed class TripDataDetailsUiIntent {
    data object OnBackNavigation : TripDataDetailsUiIntent()
}