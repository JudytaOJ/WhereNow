package com.example.wherenow.data.usecases

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.repository.TripListRepository
import javax.inject.Inject

class SaveDataTileUseCase @Inject constructor(
    private val tripListRepository: TripListRepository
) {
    suspend operator fun invoke(trip: Trip) =
        tripListRepository.saveDataTile(trip)
}