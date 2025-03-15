package com.example.wherenow.ui.app.triptiledetails

import androidx.lifecycle.SavedStateHandle
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsUiIntent
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TripTileDetailsViewModelTest {
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getListDataTileUseCase: GetListDataTileUseCase = mockk(relaxed = true)

    private lateinit var sut: TripTileDetailsViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify load data`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase.invoke() } returns createListTrip()
        initialize()
        //Act
        //Assert
        coVerify { getListDataTileUseCase.invoke() }
        Assertions.assertEquals(createListTrip(), sut.uiState.value.tripList)
        Assertions.assertEquals(TRIP_ID, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify navigate to back when onBack clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.OnBackClicked)
        //Assert
        Assertions.assertEquals(TripTileDetailsNavigationEvent.OnBack, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify update date when showTripDetails clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.ShowTripDetails)
        //Assert
        Assertions.assertEquals(true, sut.uiState.value.showBottomSheet)
        Assertions.assertEquals(TRIP_ID, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify update date when hideTripDetails clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.HideTripDetails)
        //Assert
        Assertions.assertEquals(false, sut.uiState.value.showBottomSheet)
        Assertions.assertEquals(null, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify navigate ti important notes when importantNotesDetails clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.ImportantNotesDetails(TRIP_ID))
        //Assert
        Assertions.assertEquals(TripTileDetailsNavigationEvent.ImportantNotesDetails(TRIP_ID), sut.navigationEvents.firstOrNull())
    }

    //helper methods
    private fun initialize() {
        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID

        sut = TripTileDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getListDataTileUseCase = getListDataTileUseCase,
        )
    }

    private fun createListTrip(): List<TripListItemData> =
        listOf(
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
                distance = "1234"
            )
        ).sortedBy { date -> date.date }

    //helper constants
    companion object {
        const val TRIP_ID = 2
    }
}