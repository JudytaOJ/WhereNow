package com.example.wherenow.data.usecases

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.repository.TripListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActuallyTripListUseCase @Inject constructor(
    private val tripListRepository: TripListRepository
) {
    suspend operator fun invoke(): Flow<List<Trip>> =
        tripListRepository.getTripFromThisMonth()
}