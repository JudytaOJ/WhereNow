package com.example.wherenow.ui.app.triptiledetails.model

import com.example.wherenow.repository.models.TripListItemData

data class TripTileDetailsViewState(
    val tripList: List<TripListItemData> = listOf(),
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val detailsId: Int? = null
)