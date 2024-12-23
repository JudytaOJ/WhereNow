package com.example.wherenow.ui.app.triplist.model

internal sealed class TripListUiIntent {
    data object OnCloseApp : TripListUiIntent()
    data object OnAddTrip : TripListUiIntent()
    data class OnDeleteTrip(val id: Int, val selectedButton: TripListDataEnum) : TripListUiIntent()
    data class ShowTripDetails(val id: Int) : TripListUiIntent()
    data object HideTripDetails : TripListUiIntent()
    data class OnGetListDependsButtonType(val selectedButton: TripListDataEnum) : TripListUiIntent()
}