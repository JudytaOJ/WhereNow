package com.example.wherenow.ui.app.triptiledetails.model

internal sealed class TripTileDetailsNavigationEvent {
    data object OnBack : TripTileDetailsNavigationEvent()
    data class ImportantNotesDetails(val tripId: Int) : TripTileDetailsNavigationEvent()
    data class AddFiles(val tripId: Int) : TripTileDetailsNavigationEvent()

    //Calendar events
    data object RequestCalendarPermissions : TripTileDetailsNavigationEvent()
    data object ShowCalendarPermissionDeniedMessage : TripTileDetailsNavigationEvent()
    data object ShowEventAddedMessage : TripTileDetailsNavigationEvent()
    data object ShowEventAddFailedMessage : TripTileDetailsNavigationEvent()
    data class NavigateToCalendarApp(val startTimeMillis: Long) : TripTileDetailsNavigationEvent()
}