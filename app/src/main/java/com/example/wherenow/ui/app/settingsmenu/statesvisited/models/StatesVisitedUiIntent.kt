package com.example.wherenow.ui.app.settingsmenu.statesvisited.models

internal sealed class StatesVisitedUiIntent {
    data object OnBackClicked : StatesVisitedUiIntent()
    data class OnCheckboxToggled(val id: Int, val isChecked: Boolean) : StatesVisitedUiIntent()
}