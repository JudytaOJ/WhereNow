package com.example.wherenow.ui.app.tripdatadetails.models

internal sealed class TripDataDetailsUiIntent {
    data object OnBackNavigation : TripDataDetailsUiIntent()

    //fields dependent on dropdown with cities
    data class OnUpdateFromCity(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateFromIata(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateFromCountry(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateFromAirportName(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateToCity(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateToIata(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateToCountry(val newValue: String): TripDataDetailsUiIntent()
    data class OnUpdateToAirportName(val newValue: String): TripDataDetailsUiIntent()
}