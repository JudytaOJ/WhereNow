package com.example.wherenow.data.network

import com.example.wherenow.data.dto.WhereNowAirportListDto
import retrofit2.Response
import javax.inject.Inject

interface WhereNowApiService {
    suspend fun getAirportList(): Response<List<WhereNowAirportListDto>>
}

class WhereNowApiServiceImpl @Inject constructor(
    private val apiService: WhereNowApi,
) : WhereNowApiService {

    override suspend fun getAirportList(): Response<List<WhereNowAirportListDto>> =
        apiService.getAirportList()

}