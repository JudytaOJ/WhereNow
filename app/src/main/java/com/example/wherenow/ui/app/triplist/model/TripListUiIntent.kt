package com.example.wherenow.ui.app.triplist.model

internal sealed class TripListUiIntent {
    data object OnAddTrip : TripListUiIntent()
    data class ShowTripDetails(val tileId: Int) : TripListUiIntent()
    data class OnDeleteTrip(val id: Int, val selectedButton: TripListDataEnum) : TripListUiIntent()
    data class OnGetListDependsButtonType(val selectedButton: TripListDataEnum) : TripListUiIntent()

    //Menu app
    data object StatesVisited : TripListUiIntent()
    data object OnCloseApp : TripListUiIntent()
}