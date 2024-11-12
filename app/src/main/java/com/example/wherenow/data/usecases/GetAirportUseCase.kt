package com.example.wherenow.data.usecases

import com.example.wherenow.data.network.WhereNowApiService
import com.example.wherenow.ui.triplist.model.ListDetails
import com.example.wherenow.ui.triplist.model.toDto
import javax.inject.Inject

class GetAirportUseCase @Inject constructor(
    private val whereNowApiService: WhereNowApiService
) {
    suspend operator fun invoke(): List<ListDetails?> {
        val response = whereNowApiService.getAirportList()
        val responseBody = if (response.isSuccessful) {
            response.body()?.firstOrNull()?.airportList?.firstOrNull()?.attributes
        } else {
            error(message = "Request failed")
        }
        return listOf(responseBody?.toDto())
    }
}