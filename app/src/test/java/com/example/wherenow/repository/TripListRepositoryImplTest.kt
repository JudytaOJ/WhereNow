package com.example.wherenow.repository

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.database.trip.TripDao
import com.example.wherenow.database.trip.TripDatabase
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class TripListRepositoryImplTest {

    private val tripDao: TripDao = mockk(relaxed = true)
    private val tripDatabase: TripDatabase = mockk(relaxed = true)

    private lateinit var repository: TripListRepository

    private val fakeStartDate = 1000L
    private val fakeEndDate = 5000L

    @Before
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveDataTile calls dao insertTrip and returns ID`() = runTest {
        //Arrange
        val trip = creteActuallyTrip()
        coEvery { tripDao.insertTrip(trip) } returns 42L
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.saveDataTile(trip)
        //Assert
        assertEquals(42, result)
        coVerify { tripDao.insertTrip(trip) }
    }

    @Test
    fun `getListDataTile returns all trips`() = runTest {
        //Arrange
        val trips = listOf(cretePastTrip(), creteActuallyTrip(), creteFutureTrip())
        coEvery { tripDao.getAllTrips() } returns trips
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getListDataTile()
        //Assert
        assertEquals(trips, result)
        coVerify { tripDao.getAllTrips() }
    }

    @Test
    fun `getListDataTile returns empty list when no trips in db`() = runTest {
        //Arrange
        coEvery { tripDao.getAllTrips() } returns emptyList()
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getListDataTile()
        //Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getListDataTile throws when dao fails`() = runTest {
        //Arrange
        coEvery { tripDao.getAllTrips() } throws RuntimeException("Error")
        initialize()
        advanceUntilIdle()
        //Act & Assert
        try {
            repository.getListDataTile()
            fail("Expected RuntimeException to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Error", e.message)
        }
    }

    @Test
    fun `deletedDataTile calls dao deleteTrip`() = runTest {
        //Arrange
        coEvery { tripDao.deleteTrip(1) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        repository.deletedDataTile(1)
        //Assert
        coVerify { tripDao.deleteTrip(1) }
    }

    @Test
    fun `getPastTrip calls dao with correct start date`() = runTest {
        //Arrange
        val trips = listOf(cretePastTrip())
        coEvery { tripDao.getPastTrip(fakeStartDate) } returns trips
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getPastTrip()
        //Assert
        assertEquals(trips, result)
        coVerify { tripDao.getPastTrip(fakeStartDate) }
    }

    @Test
    fun `getUpcomingTrips calls dao with start and end date`() = runTest {
        //Arrange
        val trips = listOf(creteActuallyTrip())
        coEvery { tripDao.getTripFromThisMonth(fakeStartDate, fakeEndDate) } returns trips
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getUpcomingTrips()
        //Assert
        assertEquals(trips, result)
        coVerify { tripDao.getTripFromThisMonth(fakeStartDate, fakeEndDate) }
    }

    @Test
    fun `getFutureTrip calls dao with correct end date`() = runTest {
        //Arrange
        val trips = listOf(creteFutureTrip())
        coEvery { tripDao.getFutureTrip(fakeEndDate) } returns trips
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getFutureTrip()
        //Assert
        assertEquals(trips, result)
        coVerify { tripDao.getFutureTrip(fakeEndDate) }
    }

    //helper methods
    private fun initialize() {
        coEvery { tripDatabase.dao() } returns tripDao

        repository = object : TripListRepositoryImpl(tripDatabase) {
            override val startDate = fakeStartDate
            override val endDate = fakeEndDate
        }
    }

    private fun cretePastTrip(): Trip = Trip(
        date = 20250312,
        departureCity = "New York",
        departureCountry = "United States",
        departureAirport = "John F. Kennedy International Airport",
        departureCodeAirport = "JFK",
        arrivalCity = "Chicago",
        arrivalCountry = "United States",
        arrivalAirport = "Chicago O'Hare International Airport",
        arrivalCodeAirport = "ORD",
        id = 2,
        image = WhereNowDetailsTileImageType.US_GAS_STATION.icon,
        distance = "1234"
    )

    private fun creteActuallyTrip(): Trip = Trip(
        date = convertLocalDateToTimestampUTC(LocalDate.now()),
        departureCity = "New York",
        departureCountry = "United States",
        departureAirport = "John F. Kennedy International Airport",
        departureCodeAirport = "JFK",
        arrivalCity = "Chicago",
        arrivalCountry = "United States",
        arrivalAirport = "Chicago O'Hare International Airport",
        arrivalCodeAirport = "ORD",
        id = 2,
        image = WhereNowDetailsTileImageType.US_GAS_STATION.icon,
        distance = "1234"
    )

    private fun creteFutureTrip(): Trip = Trip(
        date = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(4)),
        image = WhereNowDetailsTileImageType.US_MONUMENT_VALLEY.icon,
        departureCity = "San Francisco",
        departureCountry = "United States",
        departureAirport = "San Francisco International Airport",
        departureCodeAirport = "SFO",
        arrivalCity = "Seattle",
        arrivalCountry = "United States",
        arrivalAirport = "Seattle-Tacoma International Airport",
        arrivalCodeAirport = "SEA",
        distance = "1234",
        id = 2
    )
}