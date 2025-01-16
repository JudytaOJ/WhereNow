package com.example.wherenow.data.usecases

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.repository.TripCityRepository
import javax.inject.Inject

class GetCityListFromRepositoryUseCase @Inject constructor(
    private val tripCityRepository: TripCityRepository
) {
    suspend operator fun invoke(): List<AttributesDto> =
        tripCityRepository.getCityList()
}