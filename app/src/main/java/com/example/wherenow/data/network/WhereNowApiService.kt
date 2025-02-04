package com.example.wherenow.data.network

import com.example.wherenow.data.dto.AirportListDto
import com.example.wherenow.data.dto.DistanceAirportDto
import com.example.wherenow.data.dto.DistanceBetweenAirportDto
import retrofit2.Response
import javax.inject.Inject

interface WhereNowApiService {
    suspend fun getAirportList(page: Int): Response<AirportListDto>
    suspend fun getDistanceBetweenAirport(from: DistanceAirportDto): Response<DistanceBetweenAirportDto>
}

class WhereNowApiServiceImpl @Inject constructor(
    private val apiService: WhereNowApi,
) : WhereNowApiService {

    override suspend fun getAirportList(page: Int): Response<AirportListDto> =
        apiService.getAirportList(page = page)

    override suspend fun getDistanceBetweenAirport(from: DistanceAirportDto): Response<DistanceBetweenAirportDto> =
        apiService.getDistanceBetweenAirport(from = from)
}