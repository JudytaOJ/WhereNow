package com.example.wherenow.data.usecases

import com.example.wherenow.data.dto.AirportListDto
import com.example.wherenow.data.network.WhereNowApiService
import javax.inject.Inject

class GetAirportUseCase @Inject constructor(
    private val whereNowApiService: WhereNowApiService
) {
    suspend operator fun invoke(page: Int): List<AirportListDto?> {
        val response = whereNowApiService.getAirportList(page = page)
        val responseBody = if (response.isSuccessful) {
            response.body()
        } else {
            error(message = "Request failed")
        }
        return listOf(responseBody)
    }
}