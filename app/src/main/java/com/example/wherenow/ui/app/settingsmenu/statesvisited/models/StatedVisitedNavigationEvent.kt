package com.example.wherenow.ui.app.settingsmenu.statesvisited.models

internal sealed class StatedVisitedNavigationEvent {
    data object OnBackNavigation : StatedVisitedNavigationEvent()
}