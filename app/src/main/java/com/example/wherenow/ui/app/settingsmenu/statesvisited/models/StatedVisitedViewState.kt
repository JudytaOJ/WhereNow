package com.example.wherenow.ui.app.settingsmenu.statesvisited.models

internal data class StatedVisitedViewState(
    val statesList: List<StateItem> = emptyList(),
    val showAnimation: Boolean = false,
    val hasShownAnimation: Boolean = false
)