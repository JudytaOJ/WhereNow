package com.example.wherenow.data.usecases

import com.example.wherenow.repository.statistics.FlightStatisticsRepository

class GetFeaturesStatisticsUseCase internal constructor(
    private val flightStatisticsRepository: FlightStatisticsRepository
) {
    operator fun invoke() = flightStatisticsRepository.getFeatures()
}