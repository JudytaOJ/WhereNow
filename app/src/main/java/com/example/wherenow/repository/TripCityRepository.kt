package com.example.wherenow.repository

import com.example.wherenow.data.dto.AttributesDto

interface TripCityRepository {
    suspend fun saveCityList(list: List<AttributesDto>)
    suspend fun getCityList(): List<AttributesDto>
}

class TripCityRepositoryImpl : TripCityRepository {
    var list: List<AttributesDto> = emptyList()

    override suspend fun saveCityList(newValue: List<AttributesDto>) {
        list = list.plus(newValue)
    }

    override suspend fun getCityList(): List<AttributesDto> = list
}