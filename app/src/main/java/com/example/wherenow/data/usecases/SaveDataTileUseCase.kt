package com.example.wherenow.data.usecases

import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.repository.models.toItem

class SaveDataTileUseCase internal constructor(
    private val tripListRepository: TripListRepository
) {
    suspend operator fun invoke(trip: TripListItemData) =
        tripListRepository.saveDataTile(trip.toItem())
}