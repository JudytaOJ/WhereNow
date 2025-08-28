package com.example.wherenow.ui.app.triptiledetails

import androidx.lifecycle.SavedStateHandle
import com.example.wherenow.data.usecases.AddCalendarFlightUseCase
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.data.usecases.IsTripAddedToCalendarUseCase
import com.example.wherenow.data.usecases.ObserveTripCalendarStatusUseCase
import com.example.wherenow.data.usecases.SaveTripAddedToCalendarUseCase
import com.example.wherenow.data.usecases.SyncCalendarEventsUseCase
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsUiIntent
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.util.ResourceProvider
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TripTileDetailsViewModelTest {
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getListDataTileUseCase: GetListDataTileUseCase = mockk(relaxed = true)
    private val addCalendarEventUseCase: AddCalendarFlightUseCase = mockk(relaxed = true)
    private val saveTripAddedToCalendarUseCase: SaveTripAddedToCalendarUseCase = mockk(relaxed = true)
    private val observeTripCalendarStatusUseCase: ObserveTripCalendarStatusUseCase = mockk(relaxed = true)
    private val isTripAddedToCalendarUseCase: IsTripAddedToCalendarUseCase = mockk(relaxed = true)
    private val stringProvider: ResourceProvider = mockk(relaxed = true)
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase = mockk(relaxed = true)

    private lateinit var sut: TripTileDetailsViewModel

    @Before
    fun beforeEach() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify load data`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase.invoke() } returns createListTrip()
        initialize()
        //Act
        advanceUntilIdle()
        //Assert
        coVerify { getListDataTileUseCase.invoke() }
        assertEquals(createListTrip(), sut.uiState.value.tripList)
        assertEquals(TRIP_ID, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify navigate to back when onBack clicked`() = runTest {
        //Arrange
        initialize()
        val events = mutableListOf<TripTileDetailsNavigationEvent>()
        val job = launch { sut.navigationEvents.toList(events) }
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.OnBackClicked)
        advanceUntilIdle()
        //Assert
        assertTrue(events.contains(TripTileDetailsNavigationEvent.OnBack))
        job.cancel()
    }

    @Test
    fun `verify update date when showTripDetails clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.ShowTripDetails)
        advanceUntilIdle()
        //Assert
        assertTrue(sut.uiState.value.showBottomSheet)
        assertEquals(TRIP_ID, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify update date when hideTripDetails clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.HideTripDetails)
        advanceUntilIdle()
        //Assert
        assertEquals(false, sut.uiState.value.showBottomSheet)
        assertEquals(null, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify navigate ti important notes when importantNotesDetails clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.ImportantNotesDetails(TRIP_ID))
        //Assert
        assertEquals(TripTileDetailsNavigationEvent.ImportantNotesDetails(TRIP_ID), sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `onUiIntent AddTripToCalendar should request permissions`() = runTest {
        //Arrange
        initialize()
        val events = mutableListOf<TripTileDetailsNavigationEvent>()
        val job = launch { sut.navigationEvents.toList(events) }
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.AddTripToCalendar(TRIP_ID))
        advanceUntilIdle()
        //Assert
        assertTrue(events.contains(TripTileDetailsNavigationEvent.RequestCalendarPermissions))
        job.cancel()
    }

    @Test
    fun `permissionResult granted and calendar event added should emit success message`() = runTest {
        //Arrange
        coEvery { addCalendarEventUseCase(any()) } returns true
        coEvery { saveTripAddedToCalendarUseCase(any(), any()) } just Runs
        initialize()
        val events = mutableListOf<TripTileDetailsNavigationEvent>()
        val job = launch { sut.navigationEvents.toList(events) }
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.PermissionsResult(granted = true))
        advanceUntilIdle()
        //Assert
        assertTrue(events.contains(TripTileDetailsNavigationEvent.ShowEventAddedMessage))
        job.cancel()
    }

    @Test
    fun `permissionResult granted but calendar event failed should emit failure message`() = runTest {
        //Arrange
        coEvery { addCalendarEventUseCase(any()) } returns false
        initialize()
        val events = mutableListOf<TripTileDetailsNavigationEvent>()
        val job = launch { sut.navigationEvents.toList(events) }
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.PermissionsResult(granted = true))
        advanceUntilIdle()
        //Assert
        assertTrue(events.contains(TripTileDetailsNavigationEvent.ShowEventAddFailedMessage))
        job.cancel()
    }

    @Test
    fun `permissionResult not granted should emit permission denied message`() = runTest {
        //Arrange
        initialize()
        val events = mutableListOf<TripTileDetailsNavigationEvent>()
        val job = launch { sut.navigationEvents.toList(events) }
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.PermissionsResult(granted = false))
        advanceUntilIdle()
        //Assert
        assertTrue(events.contains(TripTileDetailsNavigationEvent.ShowCalendarPermissionDeniedMessage))
        job.cancel()
    }

    @Test
    fun `permissionResult granted and trip already in calendar should navigate to calendar app`() = runTest {
        // Arrange
        initialize()
        coEvery { observeTripCalendarStatusUseCase.invoke(TRIP_ID) } returns flowOf(true)
        coEvery { isTripAddedToCalendarUseCase.invoke(TRIP_ID) } returns true
        val events = mutableListOf<TripTileDetailsNavigationEvent>()
        val job = launch { sut.navigationEvents.toList(events) }
        // Act
        sut.onUiIntent(TripTileDetailsUiIntent.PermissionsResult(granted = true))
        advanceUntilIdle()
        // Assert
        val navigateEvent = events.find { it is TripTileDetailsNavigationEvent.NavigateToCalendarApp }
        assertTrue(navigateEvent != null, "Expected NavigateToCalendarApp to be emitted")
        job.cancel()
    }

    @Test
    fun `verifyTripExistInCalendarApp - when trip exists, syncs calendar and updates UI`() = runTest {
        //Arrange
        initialize()
        coEvery { isTripAddedToCalendarUseCase.invoke(TRIP_ID) } returns true
        //Act
        sut.onUiIntent(TripTileDetailsUiIntent.SyncCalendarApp)
        advanceUntilIdle()
        //Assert
        coVerify { syncCalendarEventsUseCase.invoke(any(), any(), any()) }
        assertTrue(sut.uiState.value.isTripAddedToCalendar)
        assertEquals(sut.uiState.value.isTripAddedToCalendar, true)
    }

    //helper methods
    private fun initialize() {
        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID
        coEvery { observeTripCalendarStatusUseCase.invoke(TRIP_ID) } returns flowOf(false)
        coEvery { getListDataTileUseCase.invoke() } returns createListTrip()
        coEvery { isTripAddedToCalendarUseCase.invoke(TRIP_ID) } returns false
        every { stringProvider.getString(any(), any()) } returns "Mocked"

        sut = TripTileDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getListDataTileUseCase = getListDataTileUseCase,
            addCalendarEventUseCase = addCalendarEventUseCase,
            saveTripAddedToCalendarUseCase = saveTripAddedToCalendarUseCase,
            observeTripCalendarStatusUseCase = observeTripCalendarStatusUseCase,
            isTripAddedToCalendarUseCase = isTripAddedToCalendarUseCase,
            stringProvider = stringProvider,
            syncCalendarEventsUseCase = syncCalendarEventsUseCase
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