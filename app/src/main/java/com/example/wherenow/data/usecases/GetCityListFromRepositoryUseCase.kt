package com.example.wherenow.data.usecases

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.repository.TripCityRepository

class GetCityListFromRepositoryUseCase internal constructor(
    private val tripCityRepository: TripCityRepository
) {
    suspend operator fun invoke(): List<AttributesDto> =
        tripCityRepository.getCityList()
}