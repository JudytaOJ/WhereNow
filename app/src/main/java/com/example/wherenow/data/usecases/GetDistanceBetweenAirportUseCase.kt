package com.example.wherenow.data.usecases

import com.example.wherenow.data.dto.DistanceAirportDto
import com.example.wherenow.data.dto.DistanceBetweenAirportDto
import com.example.wherenow.data.network.WhereNowApiService
import javax.inject.Inject

class GetDistanceBetweenAirportUseCase @Inject constructor(
    private val whereNowApiService: WhereNowApiService
) {
    suspend operator fun invoke(from: DistanceAirportDto): List<DistanceBetweenAirportDto?> {
        val response = whereNowApiService.getDistanceBetweenAirport(from = from)
        val responseBody = if (response.isSuccessful) {
            response.body()
        } else {
            error(message = "Request failed")
        }
        return listOf(responseBody)
    }
}