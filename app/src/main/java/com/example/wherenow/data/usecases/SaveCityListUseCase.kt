package com.example.wherenow.data.usecases

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.repository.TripCityRepository
import javax.inject.Inject

class SaveCityListUseCase @Inject constructor(
    private val tripCityRepository: TripCityRepository
) {
    suspend operator fun invoke(newValue: List<AttributesDto>) =
        tripCityRepository.saveCityList(newValue)
}