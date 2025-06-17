package com.example.wherenow.ui.app.triplist

import com.example.wherenow.data.usecases.CancelPushUseCase
import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetActuallyTripListUseCase
import com.example.wherenow.data.usecases.GetFutureTripListUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.util.convertLocalDateToTimestampUTC
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
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TripListViewModelTest {
    private val getPastTripListUseCase: GetPastTripListUseCase = mockk(relaxed = true)
    private val deleteTileOnListUseCase: DeleteTileOnListUseCase = mockk(relaxed = true)
    private val getActuallyTripListUseCase: GetActuallyTripListUseCase = mockk(relaxed = true)
    private val getFutureTripListUseCase: GetFutureTripListUseCase = mockk(relaxed = true)
    private val cancelPushUseCase: CancelPushUseCase = mockk(relaxed = true)

    private lateinit var sut: TripListViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        initialize()
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify navigate to close app`() = runTest {
        //Arrange
        //Act
        sut.onUiIntent(TripListUiIntent.OnCloseApp)
        //Assert
        Assertions.assertEquals(TripListNavigationEvent.OnCloseApp, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify navigate to add trip screen`() = runTest {
        //Arrange
        //Act
        sut.onUiIntent(TripListUiIntent.OnAddTrip)
        //Assert
        Assertions.assertEquals(TripListNavigationEvent.OnAddTrip, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify list depends on button type - PAST type button`() = runTest {
        //Arrange
        coEvery { getPastTripListUseCase() } returns createListWithPastTrip()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PAST))
        //Assert
        coVerify { getPastTripListUseCase() }
        Assertions.assertEquals(createListWithPastTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify list depends on button type - PRESENT type button`() = runTest {
        //Arrange
        coEvery { getActuallyTripListUseCase() } returns createListWithPresentTrip()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PRESENT))
        //Assert
        coVerify { getActuallyTripListUseCase() }
        Assertions.assertEquals(createListWithPresentTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify list depends on button type - FUTURE type button`() = runTest {
        //Arrange
        coEvery { getFutureTripListUseCase() } returns createListWithFutureTrip()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.FUTURE))
        //Assert
        coVerify { getFutureTripListUseCase() }
        Assertions.assertEquals(createListWithFutureTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - PAST type button`() = runTest {
        //Arrange
        coEvery { deleteTileOnListUseCase(id = 1) } returns mockk(relaxed = true)
        coEvery { getPastTripListUseCase() } returns createListWithPastTrip()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 1, selectedButton = TripListDataEnum.PAST))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PAST))
        //Assert
        coVerify { deleteTileOnListUseCase(id = 1) }
        coVerify { getPastTripListUseCase() }
        Assertions.assertEquals(createListWithPastTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - PRESENT type button`() = runTest {
        //Arrange
        coEvery { deleteTileOnListUseCase(id = 4) } returns mockk(relaxed = true)
        coEvery { getActuallyTripListUseCase() } returns createListWithPresentTrip()
        coEvery { cancelPushUseCase.invoke(any()) } returns mockk(relaxed = true)
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 4, selectedButton = TripListDataEnum.PRESENT))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PRESENT))
        //Assert
        coVerify { deleteTileOnListUseCase(id = 4) }
        coVerify { getActuallyTripListUseCase() }
        coVerify { cancelPushUseCase.invoke(any()) }
        Assertions.assertEquals(createListWithPresentTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - FUTURE type button`() = runTest {
        //Arrange
        coEvery { getFutureTripListUseCase.invoke() } returns createListWithFutureTrip()
        coEvery { deleteTileOnListUseCase(id = 5) } returns mockk(relaxed = true)
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.FUTURE))
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 5, selectedButton = TripListDataEnum.FUTURE))
        advanceUntilIdle()
        //Assert
        Assertions.assertEquals(TripListDataEnum.FUTURE, sut.uiState.value.selectedButtonType)
        coVerify { getFutureTripListUseCase.invoke() }
        coVerify { deleteTileOnListUseCase(id = 5) }
        Assertions.assertEquals(createListWithFutureTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify navigate when ShowTripDetails clicked`() = runTest {
        //Arrange
        coEvery { getActuallyTripListUseCase() } returns createListWithPastTrip()
        //Act
        sut.onUiIntent(TripListUiIntent.ShowTripDetails(tileId = 3))
        //Assert
        coVerify { getActuallyTripListUseCase() }
        Assertions.assertEquals(TripListNavigationEvent.OnShowDetailsTrip(tileId = 3), sut.navigationEvents.firstOrNull())
    }

    //helper methods
    private fun initialize() {
        sut = TripListViewModel(
            deleteTileOnListUseCase = deleteTileOnListUseCase,
            getPastTripListUseCase = getPastTripListUseCase,
            getActuallyTripListUseCase = getActuallyTripListUseCase,
            getFutureTripListUseCase = getFutureTripListUseCase,
            cancelPushUseCase = cancelPushUseCase
        )
    }

    private fun createListWithPastTrip(): List<TripListItemData> =
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
        ).sortedBy { date -> date.date }.reversed()

    private fun createListWithPresentTrip(): List<TripListItemData> =
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
                id = 3,
                image = WhereNowDetailsTileImageType.US_AUTUMN.icon,
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
                id = 4,
                image = WhereNowDetailsTileImageType.US_NEVADA.icon,
                distance = "1234"
            )
        ).sortedBy { date -> date.date }

    private fun createListWithFutureTrip(): List<TripListItemData> =
        listOf(
            TripListItemData(
                date = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(4).plusYears(4)),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 5,
                image = WhereNowDetailsTileImageType.US_DESERT.icon,
                distance = "1234"
            ),
            TripListItemData(
                date = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(4).plusYears(1)),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 6,
                image = WhereNowDetailsTileImageType.US_HAWAII.icon,
                distance = "1234"
            )
        ).sortedBy { date -> date.date }
}