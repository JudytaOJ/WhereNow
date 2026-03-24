package com.example.wherenow.ui.app.settingsmenu.flightStatistics

import com.example.wherenow.R
import com.example.wherenow.data.usecases.GetFeaturesStatisticsUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsUiIntent
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesProvider
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FlightStatisticsViewModelTest {
    private val getFeaturesStatisticsUseCase: GetFeaturesStatisticsUseCase = mockk(relaxed = true)
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase = mockk(relaxed = true)
    private val statesProvider: StatesProvider = mockk(relaxed = true)
    private val getPastTripListUseCase: GetPastTripListUseCase = mockk(relaxed = true)

    private lateinit var sut: FlightStatisticsViewModel

    @Before
    fun beforeEach() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify navigate to back when click back arrow`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(FlightStatisticsUiIntent.OnBackClicked)
        //Assert
        assertEquals(FlightStatisticsNavigationEvent.OnBackNavigation, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `should load features on init`() = runTest {
        //Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        coVerify { getFeaturesStatisticsUseCase.invoke() }
        coVerify { getPastTripListUseCase.invoke() }
        coVerify { getStatesVisitedUseCase.invoke(any()) }
        verify { statesProvider.provide() }
    }

    @Test
    fun `should update only checked states`() = runTest {
        //Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        assertEquals(listOf("Alabama", "Wyoming"), sut.uiState.value.statedVisited)
    }

    @Test
    fun `should calculate total flights and total distance`() = runTest {
        // Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        val state = sut.uiState.value
        assertEquals(3, state.totalFlight)
        assertEquals(3146, state.totalDistance)
    }

    @Test
    fun `should calculate longest and shortest flight`() = runTest {
        // Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        val state = sut.uiState.value
        assertEquals(1234, state.longestFlight)
        assertEquals(678, state.shortestFlight)
    }

    @Test
    fun `should calculate most frequent route`() = runTest {
        // Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        val state = sut.uiState.value
        assertEquals("New York-Chicago", state.mostFrequentRoute)
    }

    @Test
    fun `should calculate top arrival and departure city`() = runTest {
        // Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        val state = sut.uiState.value
        assertEquals("Chicago", state.topArrivalCity)
        assertEquals("New York", state.topDestinationCity)
    }

    @Test
    fun `should count flights in current month`() = runTest {
        // Arrange
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        assertEquals(3, sut.uiState.value.flightsPerMonth)
    }

    //helper methods
    private fun initialize() {
        every { statesProvider.provide() } returns mockk()
        coEvery { getStatesVisitedUseCase.invoke(any()) } returns createCheckedStatesList()
        coEvery { getFeaturesStatisticsUseCase.invoke() } returns mockk(relaxed = true)
        coEvery { getPastTripListUseCase.invoke() } returns createListWithPastTrip()

        sut = FlightStatisticsViewModel(
            getFeaturesStatisticsUseCase = getFeaturesStatisticsUseCase,
            getStatesVisitedUseCase = getStatesVisitedUseCase,
            statesProvider = statesProvider,
            getPastTripListUseCase = getPastTripListUseCase
        )
    }

    private fun createCheckedStatesList(): List<StateItem> = listOf(
        StateItem(
            text = "Alabama",
            imageRes = R.drawable.flag_of_alabama,
            id = 1,
            isChecked = true
        ), StateItem(
            text = "Wyoming",
            imageRes = R.drawable.flag_of_wyoming,
            id = 4,
            isChecked = true
        )
    )

    private fun createListWithPastTrip(): List<TripListItemData> = listOf(
        TripListItemData(
            date = convertLocalDateToTimestampUTC(LocalDate.now()),
            departureCity = "New York",
            departureCountry = "United States",
            departureAirport = "John F. Kennedy International Airport",
            departureCodeAirport = "JFK",
            arrivalCity = "Chicago",
            arrivalCountry = "United States",
            arrivalAirport = "Chicago O'Hare International Airport",
            arrivalCodeAirport = "ORD",
            id = 1,
            image = WhereNowDetailsTileImageType.US_CHINATOWN.icon,
            distance = "1234"
        ),
        TripListItemData(
            date = convertLocalDateToTimestampUTC(LocalDate.now()),
            departureCity = "New York",
            departureCountry = "United States",
            departureAirport = "John F. Kennedy International Airport",
            departureCodeAirport = "JFK",
            arrivalCity = "Chicago",
            arrivalCountry = "United States",
            arrivalAirport = "Chicago O'Hare International Airport",
            arrivalCodeAirport = "ORD",
            id = 1,
            image = WhereNowDetailsTileImageType.US_CHINATOWN.icon,
            distance = "1234"
        ),
        TripListItemData(
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
            distance = "678"
        )
    )
}