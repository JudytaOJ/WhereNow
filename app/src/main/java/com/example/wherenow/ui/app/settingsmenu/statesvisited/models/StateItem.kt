package com.example.wherenow.ui.app.settingsmenu.statesvisited.models

data class StateItem(
    val text: Int,
    val imageRes: Int,
    val id: Int,
    var isChecked: Boolean = false
)