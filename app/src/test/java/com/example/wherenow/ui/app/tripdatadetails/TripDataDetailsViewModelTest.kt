package com.example.wherenow.ui.app.tripdatadetails

import com.example.wherenow.data.dto.AirportListDto
import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.data.dto.DataItemDto
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class TripDataDetailsViewModelTest {
    private val getAirportUseCase: GetAirportUseCase = mockk(relaxed = true)
    private val tripListRepository: TripListRepository = mockk(relaxed = true)

    private lateinit var sut: TripDataDetailsViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify load date on init`() = runTest {
        //Arrange
        coEvery { getAirportUseCase.invoke() } returns createDataList()
        initialize()
        //Act
        //Assert
        coVerify { getAirportUseCase.invoke() }
        Assertions.assertEquals(createDataList().find { it.airportList.isNotEmpty() }?.airportList, sut.uiState.value.cityList)
        Assertions.assertEquals(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(), sut.uiState.value.date)
    }

    @Test
    fun `verify navigation to error screen when getAirportUseCase throw exception`() = runTest {
        //Arrange
        coEvery { getAirportUseCase.invoke() } throws IOException()
        initialize()
        //Act
        //Assert
        coVerify { getAirportUseCase.invoke() }
        Assertions.assertEquals(TripDataDetailsNavigationEvent.OnErrorScreen, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify navigate to back when click back arrow`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnBackNavigation)
        //Assert
        Assertions.assertEquals(TripDataDetailsNavigationEvent.OnBackNavigation, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify new value on update departure city`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCity(DEPARTURE_CITY))
        //Assert
        Assertions.assertEquals(DEPARTURE_CITY, sut.uiState.value.arrivalCity)
        Assertions.assertEquals(false, sut.uiState.value.isErrorDepartureCity)
    }

    @Test
    fun `verify new value on update departure airport code`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportCode(DEPARTURE_AIRPORT_CODE))
        //Assert
        Assertions.assertEquals(DEPARTURE_AIRPORT_CODE, sut.uiState.value.arrivalCodeAirport)
    }

    @Test
    fun `verify new value on update departure country`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCountry(DEPARTURE_COUNTRY))
        //Assert
        Assertions.assertEquals(DEPARTURE_COUNTRY, sut.uiState.value.arrivalCountry)
    }

    @Test
    fun `verify new value on update departure airport name`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportName(DEPARTURE_AIRPORT_NAME))
        //Assert
        Assertions.assertEquals(DEPARTURE_AIRPORT_NAME, sut.uiState.value.arrivalAirport)
    }

    @Test
    fun `verify new value on update arrival city`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(ARRIVAL_CITY))
        //Assert
        Assertions.assertEquals(ARRIVAL_CITY, sut.uiState.value.departureCity)
        Assertions.assertEquals(false, sut.uiState.value.isErrorArrivalCity)
    }

    @Test
    fun `verify new value on update arrival airport code`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportCode(ARRIVAL_AIRPORT_CODE))
        //Assert
        Assertions.assertEquals(ARRIVAL_AIRPORT_CODE, sut.uiState.value.departureCodeAirport)
    }

    @Test
    fun `verify new value on update arrival country`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCountry(ARRIVAL_COUNTRY))
        //Assert
        Assertions.assertEquals(ARRIVAL_COUNTRY, sut.uiState.value.departureCountry)
    }

    @Test
    fun `verify new value on update arrival airport name`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportName(ARRIVAL_AIRPORT_NAME))
        //Assert
        Assertions.assertEquals(ARRIVAL_AIRPORT_NAME, sut.uiState.value.departureAirport)
    }

    @Test
    fun `verify new value on update date`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDate(DATE))
        //Assert
        Assertions.assertEquals(DATE, sut.uiState.value.date)
    }

    @Test
    fun `verify date when onNextClicked - isErrorDepartureCity and isErrorArrivalCity is true`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnNextClicked)
        //Assert
        Assertions.assertEquals(true, sut.uiState.value.isErrorDepartureCity)
        Assertions.assertEquals(true, sut.uiState.value.isErrorArrivalCity)
    }

    @Test
    fun `verify date when onNextClicked - isErrorDepartureCity and isErrorArrivalCity is false`() = runTest {
        //Arrange
        coEvery { tripListRepository.saveDataTile(any()) } returns Unit
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(ARRIVAL_CITY))
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCity(DEPARTURE_CITY))
        sut.onUiIntent(TripDataDetailsUiIntent.OnNextClicked)
        //Assert
        Assertions.assertEquals(false, sut.uiState.value.isErrorDepartureCity)
        Assertions.assertEquals(false, sut.uiState.value.isErrorArrivalCity)
        coVerify { tripListRepository.saveDataTile(any()) }
        Assertions.assertEquals(TripDataDetailsNavigationEvent.OnNextClicked, sut.navigationEvents.firstOrNull())
    }

    //helper constants
    companion object {
        const val DEPARTURE_CITY = "Oslo"
        const val DEPARTURE_AIRPORT_CODE = "ATH"
        const val DEPARTURE_COUNTRY = "Norway"
        const val DEPARTURE_AIRPORT_NAME = "Berlin Brandenburg Airport"
        const val ARRIVAL_CITY = "Paris"
        const val ARRIVAL_AIRPORT_CODE = "JFK"
        const val ARRIVAL_COUNTRY = "Brasil"
        const val ARRIVAL_AIRPORT_NAME = "Cairo International Airport"
        const val DATE = 12122025L
    }

    //helper methods
    private fun initialize() {
        sut = TripDataDetailsViewModel(
            tripListRepository = tripListRepository,
            getAirportUseCase = getAirportUseCase
        )
    }

    private fun createDataList() = listOf(
        AirportListDto(
            airportList = listOf(
                AttributesDto(
                    attributes = DataItemDto(
                        city = "Warsaw",
                        country = "Poland",
                        iata = "WAW",
                        icao = "EPWA",
                        name = "Warsaw Chopin"
                    ),
                    id = "1",
                    type = "1"
                )
            )
        ),
        AirportListDto(
            airportList = listOf(
                AttributesDto(
                    attributes = DataItemDto(
                        city = "London",
                        country = "United Kingdom",
                        iata = "LHR",
                        icao = "EPWA",
                        name = "Heathrow Airport"
                    ),
                    id = "2",
                    type = "2"
                )
            )
        ),
        AirportListDto(
            airportList = listOf(
                AttributesDto(
                    attributes = DataItemDto(
                        city = "Cairo",
                        country = "Egypt",
                        iata = "CAI",
                        icao = "EPWA",
                        name = "Cairo International Airport"
                    ),
                    id = "3",
                    type = "3"
                )
            )
        )
    )
}