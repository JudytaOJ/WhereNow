package com.example.wherenow.data.network

import com.example.wherenow.data.dto.AirportListDto
import com.example.wherenow.data.dto.DistanceAirportDto
import com.example.wherenow.data.dto.DistanceBetweenAirportDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WhereNowApi {
    @GET("https://airportgap.com/api/airports")
    suspend fun getAirportList(
        @Query("page") page: Int,
    ): Response<AirportListDto>

    @POST("https://airportgap.com/api/airports/distance")
    suspend fun getDistanceBetweenAirport(
        @Body from: DistanceAirportDto,
    ): Response<DistanceBetweenAirportDto>
}