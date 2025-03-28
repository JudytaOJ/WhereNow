package com.example.wherenow.ui.app.tripdatadetails.models

internal sealed class TripDataDetailsUiIntent {
    data object OnBackNavigation : TripDataDetailsUiIntent()
    data object OnNextClicked : TripDataDetailsUiIntent()

    //fields dependent on dropdown with cities
    data class OnUpdateDepartureCity(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateDepartureAirportCode(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateDepartureCountry(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateDepartureAirportName(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateArrivalCity(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateArrivalAirportCode(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateArrivalCountry(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateArrivalAirportName(val newValue: String) : TripDataDetailsUiIntent()
    data class OnUpdateDate(val newValue: Long) : TripDataDetailsUiIntent()
    data class OnUpdateFromSearchText(val text: String) : TripDataDetailsUiIntent()
    data class OnUpdateToSearchText(val text: String) : TripDataDetailsUiIntent()

    data object ShowModalFromCityList : TripDataDetailsUiIntent()
    data object HideModalFromCityList : TripDataDetailsUiIntent()
    data object ShowModalToCityList : TripDataDetailsUiIntent()
    data object HideModalToCityList : TripDataDetailsUiIntent()
}