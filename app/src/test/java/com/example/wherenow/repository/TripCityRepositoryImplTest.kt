package com.example.wherenow.repository

import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.data.dto.DataItemDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TripCityRepositoryImplTest {

    private lateinit var repository: TripCityRepository

    @Before
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCityList returns empty list initially`() = runTest {
        //Arrange
        initialize()
        advanceUntilIdle()
        val result = repository.getCityList()
        //Act
        //Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `saveCityList adds items to the list`() = runTest {
        //Arrange
        val cities = listOf(
            createAttributesDto1(),
            createAttributesDto2()
        )
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveCityList(cities)
        val result = repository.getCityList()
        //Assert
        assertEquals(cities, result)
    }

    @Test
    fun `saveCityList adds to existing list not replacing`() = runTest {
        //Arrange
        val first = listOf(createAttributesDto1())
        val second = listOf(createAttributesDto2())
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveCityList(first)
        repository.saveCityList(second)
        val result = repository.getCityList()
        //Assert
        assertEquals(2, result.size)
        assertTrue(result.containsAll(first + second))
    }

    @Test
    fun `saveCityList with empty list does not change existing data`() = runTest {
        //Arrange
        val cities = listOf(createAttributesDto1())
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveCityList(cities)
        repository.saveCityList(emptyList())
        val result = repository.getCityList()
        //Assert
        assertEquals(cities, result)
    }

    //helper methods
    private fun initialize() {
        repository = TripCityRepositoryImpl()
    }

    private fun createAttributesDto1(): AttributesDto = AttributesDto(
        attributes = DataItemDto(
            city = "Akron",
            country = "United States",
            iata = "CAK",
            icao = "KCAK",
            name = "Akron-Canton Airport"
        ),
        id = "1",
        type = "1"
    )

    private fun createAttributesDto2(): AttributesDto = AttributesDto(
        attributes = DataItemDto(
            city = "Los Angeles",
            country = "United States",
            iata = "LAX",
            icao = "KLAX",
            name = "Los Angeles International Airport"
        ),
        id = "2",
        type = "2"
    )
}