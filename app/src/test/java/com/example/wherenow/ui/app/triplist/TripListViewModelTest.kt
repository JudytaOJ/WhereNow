package com.example.wherenow.ui.app.triplist

import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.database.Trip
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
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
    private val getListDataTileUseCase: GetListDataTileUseCase = mockk(relaxed = true)
    private val deleteTileOnListUseCase: DeleteTileOnListUseCase = mockk(relaxed = true)

    private lateinit var sut: TripListViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify navigate to close app`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnCloseApp)
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(TripListNavigationEvent.OnCloseApp, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify navigate to add trip screen`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnAddTrip)
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(TripListNavigationEvent.OnAddTrip, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify list depends on button type - PAST type button`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PAST))
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(createListWithPastTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify list depends on button type - PRESENT type button`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPresentTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PRESENT))
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(createListWithPresentTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify list depends on button type - FUTURE type button`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithFutureTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.FUTURE))
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(createListWithFutureTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - PAST type button`() = runTest {
        //Arrange
        coEvery { deleteTileOnListUseCase(id = 1) } returns mockk(relaxed = true)
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 1, selectedButton = TripListDataEnum.PAST))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PAST))
        //Assert
        coVerify { deleteTileOnListUseCase(id = 1) }
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(createListWithPastTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - PRESENT type button`() = runTest {
        //Arrange
        coEvery { deleteTileOnListUseCase(id = 4) } returns mockk(relaxed = true)
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPresentTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 4, selectedButton = TripListDataEnum.PRESENT))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PRESENT))
        //Assert
        coVerify { deleteTileOnListUseCase(id = 4) }
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(createListWithPresentTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - FUTURE type button`() = runTest {
        //Arrange
        coEvery { deleteTileOnListUseCase(id = 5) } returns mockk(relaxed = true)
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithFutureTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 5, selectedButton = TripListDataEnum.FUTURE))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.FUTURE))
        //Assert
        coVerify { deleteTileOnListUseCase(id = 5) }
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(createListWithFutureTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify visibility modal - ON`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.ShowTripDetails(id = 1))
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(true, sut.uiState.value.showBottomSheet)
        Assertions.assertEquals(1, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify visibility modal - OFF`() = runTest {
        //Arrange
        coEvery { getListDataTileUseCase() } returns flow { emit(createListWithPresentTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.HideTripDetails)
        //Assert
        coVerify { getListDataTileUseCase() }
        Assertions.assertEquals(false, sut.uiState.value.showBottomSheet)
        Assertions.assertEquals(null, sut.uiState.value.detailsId)
    }

    //helper methods
    private fun initialize() {
        sut = TripListViewModel(
            getListDataTileUseCase = getListDataTileUseCase,
            deleteTileOnListUseCase = deleteTileOnListUseCase
        )
    }

    private fun createListWithPastTrip(): List<Trip> =
        listOf(
            Trip(
                date = "23.11.".plus(LocalDate.now().minusYears(1).year),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 1,
                image = WhereNowDetailsTileImageType.US_CHINATOWN.icon
            ),
            Trip(
                date = "08.11.".plus(LocalDate.now().minusYears(1).year),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 2,
                image = WhereNowDetailsTileImageType.US_GAS_STATION.icon
            )
        ).sortedBy { sort -> sort.date }
            .reversed()

    private fun createListWithPresentTrip(): List<Trip> =
        listOf(
            Trip(
                date = "23.11.".plus(LocalDate.now().year),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 3,
                image = WhereNowDetailsTileImageType.US_AUTUMN.icon
            ),
            Trip(
                date = "20.11.".plus(LocalDate.now().year),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 4,
                image = WhereNowDetailsTileImageType.US_NEVADA.icon
            )
        ).sortedBy { sort -> sort.date }
            .reversed()

    private fun createListWithFutureTrip(): List<Trip> =
        listOf(
            Trip(
                date = "08.11.".plus(LocalDate.now().plusYears(1).year),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 5,
                image = WhereNowDetailsTileImageType.US_DESERT.icon
            ),
            Trip(
                date = "08.10.".plus(LocalDate.now().plusYears(1).year),
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Chicago",
                arrivalCountry = "United States",
                arrivalAirport = "Chicago O'Hare International Airport",
                arrivalCodeAirport = "ORD",
                id = 6,
                image = WhereNowDetailsTileImageType.US_HAWAII.icon
            )
        ).sortedBy { sort -> sort.date }
            .reversed()
}