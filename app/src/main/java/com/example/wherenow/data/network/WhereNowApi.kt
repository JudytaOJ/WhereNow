package com.example.wherenow.data.network

import com.example.wherenow.data.Const.API_KEY
import com.example.wherenow.data.dto.WhereNowAirportListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WhereNowApi {

    @GET("https://airportgap.com/api/airports")
    suspend fun getAirportList(
        @Query("apikey") apikey: String = API_KEY
    ): Response<List<WhereNowAirportListDto>>

}