package com.example.wherenow.data.usecases

import com.example.wherenow.repository.TripListRepository

class DeleteTileOnListUseCase internal constructor(
    private val tripListRepository: TripListRepository
) {
    suspend operator fun invoke(id: Int) =
        tripListRepository.deletedDataTile(id)
}