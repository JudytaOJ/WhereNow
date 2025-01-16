package com.example.wherenow.data.usecases

import com.example.wherenow.repository.TripListRepository
import javax.inject.Inject

class DeleteTileOnListUseCase @Inject constructor(
    private val tripListRepository: TripListRepository
) {
    suspend operator fun invoke(id: Int) =
        tripListRepository.deletedDataTile(id)
}