package com.example.wherenow.ui.app.triptiledetails.model

internal sealed class TripTileDetailsUiIntent {
    data object OnBackClicked : TripTileDetailsUiIntent()
    data object ShowTripDetails : TripTileDetailsUiIntent()
    data object HideTripDetails : TripTileDetailsUiIntent()
    data class ImportantNotesDetails(val tripId: Int) : TripTileDetailsUiIntent()
}