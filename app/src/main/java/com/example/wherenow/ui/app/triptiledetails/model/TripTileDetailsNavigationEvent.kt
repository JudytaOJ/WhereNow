package com.example.wherenow.ui.app.triptiledetails.model

internal sealed class TripTileDetailsNavigationEvent {
    data object OnBack : TripTileDetailsNavigationEvent()
    data object ImportantNotesDetails : TripTileDetailsNavigationEvent()
}