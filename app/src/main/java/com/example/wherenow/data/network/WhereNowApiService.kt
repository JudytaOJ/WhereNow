package com.example.wherenow.data.network

import com.example.wherenow.data.dto.AirportListDto
import retrofit2.Response
import javax.inject.Inject

interface WhereNowApiService {
    suspend fun getAirportList(page: Int): Response<AirportListDto>
}

class WhereNowApiServiceImpl @Inject constructor(
    private val apiService: WhereNowApi,
) : WhereNowApiService {

    override suspend fun getAirportList(page: Int): Response<AirportListDto> =
        apiService.getAirportList(page = page)
}