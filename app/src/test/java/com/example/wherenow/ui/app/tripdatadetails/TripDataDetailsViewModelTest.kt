package com.example.wherenow.ui.app.tripdatadetails

import com.example.wherenow.data.dto.AirportListDto
import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.data.dto.DataItemDto
import com.example.wherenow.data.dto.DistanceAttributesDto
import com.example.wherenow.data.dto.DistanceBetweenAirportDto
import com.example.wherenow.data.dto.DistanceDto
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.data.usecases.GetCityListFromRepositoryUseCase
import com.example.wherenow.data.usecases.GetDistanceBetweenAirportUseCase
import com.example.wherenow.data.usecases.SaveCityListUseCase
import com.example.wherenow.data.usecases.SaveDataTileUseCase
import com.example.wherenow.data.usecases.SendPushUseCase
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class TripDataDetailsViewModelTest {
    private val getAirportUseCase: GetAirportUseCase = mockk(relaxed = true)
    private val saveDataTileUseCase: SaveDataTileUseCase = mockk(relaxed = true)
    private val saveCityListUseCase: SaveCityListUseCase = mockk(relaxed = true)
    private val getCityListFromRepositoryUseCase: GetCityListFromRepositoryUseCase = mockk(relaxed = true)
    private val getDistanceBetweenAirport: GetDistanceBetweenAirportUseCase = mockk(relaxed = true)
    private val sendPushUseCase: SendPushUseCase = mockk(relaxed = true)

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
        coEvery { getCityListFromRepositoryUseCase.invoke() } returns createListAttributesDto()
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        coVerify { getCityListFromRepositoryUseCase.invoke() }
        Assertions.assertEquals(createDataList().find { it.airportList.isNotEmpty() }?.airportList, listOf(sut.uiState.value.cityList.first()))
        Assertions.assertEquals(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), sut.uiState.value.date)
    }

    @Test
    fun `verify navigation to error screen when getCityListFromRepositoryUseCase throw exception`() = runTest {
        //Arrange
        coEvery { getCityListFromRepositoryUseCase.invoke() } throws IOException()
        initialize()
        //Act
        //Assert
        coVerify { getCityListFromRepositoryUseCase.invoke() }
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
        coEvery { saveDataTileUseCase(any()) } returns mockk(relaxed = true)
        coEvery { getDistanceBetweenAirport.invoke(any()) } returns createDistanceBetweenAirportDto()
        coEvery { sendPushUseCase.invoke(any(), any()) } returns mockk(relaxed = true)
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(ARRIVAL_CITY))
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCity(DEPARTURE_CITY))
        sut.onUiIntent(TripDataDetailsUiIntent.OnNextClicked)
        //Assert
        Assertions.assertEquals(false, sut.uiState.value.isErrorDepartureCity)
        Assertions.assertEquals(false, sut.uiState.value.isErrorArrivalCity)
        coVerify { saveDataTileUseCase(any()) }
        coVerify { getDistanceBetweenAirport.invoke(any()) }
        coVerify { sendPushUseCase.invoke(any(), any()) }
        Assertions.assertEquals(TripDataDetailsNavigationEvent.OnNextClicked, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify show bottom modal when ShowModalFromCityList clicked - from city list`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.ShowModalFromCityList)
        //Assert
        Assertions.assertEquals(true, sut.uiState.value.showBottomSheetFromCityList)
    }

    @Test
    fun `verify hide bottom modal when HideModalFromCityList clicked - from city list`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.HideModalFromCityList)
        //Assert
        Assertions.assertEquals(false, sut.uiState.value.showBottomSheetFromCityList)
    }

    @Test
    fun `verify show bottom modal when ShowModalToCityList clicked - to city list`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.ShowModalToCityList)
        //Assert
        Assertions.assertEquals(true, sut.uiState.value.showBottomSheetToCityList)
    }

    @Test
    fun `verify hide bottom modal when HideModalToCityList clicked - to city list`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.HideModalToCityList)
        //Assert
        Assertions.assertEquals(false, sut.uiState.value.showBottomSheetToCityList)
    }

    @Test
    fun `verify new value on update search from city name`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateFromSearchText("Los"))
        //Assert
        Assertions.assertEquals("Los", sut.uiState.value.searchTextFrom)
    }

    @Test
    fun `verify new value on update search to city name`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnUpdateToSearchText("Los"))
        //Assert
        Assertions.assertEquals("Los", sut.uiState.value.searchTextTo)
    }

    @Test
    fun `verify clear from text in search text bar`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnClearFromSearchText)
        //Assert
        Assertions.assertEquals(EMPTY, sut.uiState.value.searchTextFrom)
    }

    @Test
    fun `verify clear to text in search text bar`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripDataDetailsUiIntent.OnClearToSearchText)
        //Assert
        Assertions.assertEquals(EMPTY, sut.uiState.value.searchTextTo)
    }

    //helper constants
    companion object {
        const val DEPARTURE_CITY = "Chicago"
        const val DEPARTURE_AIRPORT_CODE = "JFK"
        const val DEPARTURE_COUNTRY = "United States"
        const val DEPARTURE_AIRPORT_NAME = "Los Angeles International Airport"
        const val ARRIVAL_CITY = "Detroit"
        const val ARRIVAL_AIRPORT_CODE = "LAX"
        const val ARRIVAL_COUNTRY = "United States"
        const val ARRIVAL_AIRPORT_NAME = "Miami International Airport"
        const val DATE = 12122025L
        const val EMPTY = ""
    }

    //helper methods
    private fun initialize() {
        sut = TripDataDetailsViewModel(
            getAirportUseCase = getAirportUseCase,
            saveDataTileUseCase = saveDataTileUseCase,
            getCityListFromRepositoryUseCase = getCityListFromRepositoryUseCase,
            saveCityListUseCase = saveCityListUseCase,
            getDistanceBetweenAirport = getDistanceBetweenAirport,
            sendPushUseCase = sendPushUseCase
        )
    }

    private fun createDistanceBetweenAirportDto() = listOf(
        DistanceBetweenAirportDto(
            distanceAirportList = DistanceDto(
                attributes = DistanceAttributesDto(
                    fromAirport = DataItemDto(
                        city = "Akron",
                        country = "United States",
                        iata = "CAK",
                        icao = "KCAK",
                        name = "Akron-Canton Airport"
                    ),
                    toAirport = DataItemDto(
                        city = "Los Angeles",
                        country = "United States",
                        iata = "LAX",
                        icao = "KLAX",
                        name = "Los Angeles International Airport"
                    ),
                    miles = "1234"
                ),
                id = "1",
                type = "1"
            )
        )
    )

    private fun createDataList() = listOf(
        AirportListDto(
            airportList = listOf(
                AttributesDto(
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
            )
        ),
        AirportListDto(
            airportList = listOf(
                AttributesDto(
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
            )
        ),
        AirportListDto(
            airportList = listOf(
                AttributesDto(
                    attributes = DataItemDto(
                        city = "Miami",
                        country = "United States",
                        iata = "MIA",
                        icao = "KMIA",
                        name = "Miami International Airport"
                    ),
                    id = "3",
                    type = "3"
                )
            )
        )
    )

    private fun createListAttributesDto(): List<AttributesDto> =
        listOf(
            AttributesDto(
                attributes = DataItemDto(
                    city = "Akron",
                    country = "United States",
                    iata = "CAK",
                    icao = "KCAK",
                    name = "Akron-Canton Airport"
                ),
                id = "1",
                type = "1"
            ),
            AttributesDto(
                attributes = DataItemDto(
                    city = "Los Angeles",
                    country = "United States",
                    iata = "LAX",
                    icao = "KLAX",
                    name = "Los Angeles International Airport"
                ),
                id = "2",
                type = "2"
            ),
            AttributesDto(
                attributes = DataItemDto(
                    city = "Miami",
                    country = "United States",
                    iata = "MIA",
                    icao = "KMIA",
                    name = "Miami International Airport"
                ),
                id = "3",
                type = "3"
            )
        )
}