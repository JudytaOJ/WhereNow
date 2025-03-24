package com.example.wherenow.data.usecases

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.repository.TripCityRepository

class SaveCityListUseCase internal constructor(
    private val tripCityRepository: TripCityRepository
) {
    suspend operator fun invoke(newValue: List<AttributesDto>) =
        tripCityRepository.saveCityList(newValue)
}