package com.example.wherenow.ui.app.triptiledetails.model

internal sealed class TripTileDetailsUiIntent {
    data object OnBackClicked : TripTileDetailsUiIntent()
    data object ShowTripDetails : TripTileDetailsUiIntent()
    data object HideTripDetails : TripTileDetailsUiIntent()
    data class ImportantNotesDetails(val tripId: Int) : TripTileDetailsUiIntent()
    data class AddFiles(val tripId: Int) : TripTileDetailsUiIntent()
    data class AddTripToCalendar(val tripId: Int) : TripTileDetailsUiIntent()
    data class PermissionsResult(val granted: Boolean) : TripTileDetailsUiIntent()
    data object SyncCalendarApp : TripTileDetailsUiIntent()
}