package com.example.wherenow.ui.app.triptiledetails.model

internal sealed class TripTileDetailsNavigationEvent {
    data object OnBack : TripTileDetailsNavigationEvent()
    data class ImportantNotesDetails(val tripId: Int) : TripTileDetailsNavigationEvent()
    data class AddFiles(val tripId: Int) : TripTileDetailsNavigationEvent()
}