package com.example.wherenow.ui.app.triplist

import com.example.wherenow.database.Trip
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
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
    private val repository: TripListRepository = mockk(relaxed = true)

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
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnCloseApp)
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(TripListNavigationEvent.OnCloseApp, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify navigate to add trip screen`() = runTest {
        //Arrange
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnAddTrip)
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(TripListNavigationEvent.OnAddTrip, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify list depends on button type - PAST type button`() = runTest {
        //Arrange
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PAST))
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(createListWithPastTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify list depends on button type - PRESENT type button`() = runTest {
        //Arrange
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPresentTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PRESENT))
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(createListWithPresentTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify list depends on button type - FUTURE type button`() = runTest {
        //Arrange
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithFutureTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.FUTURE))
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(createListWithFutureTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - PAST type button`() = runTest {
        //Arrange
        coEvery { repository.deletedDataTile(id = 1) } returns mockk(relaxed = true)
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 1, selectedButton = TripListDataEnum.PAST))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PAST))
        //Assert
        coVerify { repository.deletedDataTile(id = 1) }
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(createListWithPastTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - PRESENT type button`() = runTest {
        //Arrange
        coEvery { repository.deletedDataTile(id = 4) } returns mockk(relaxed = true)
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPresentTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 4, selectedButton = TripListDataEnum.PRESENT))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.PRESENT))
        //Assert
        coVerify { repository.deletedDataTile(id = 4) }
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(createListWithPresentTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify delete action on list - FUTURE type button`() = runTest {
        //Arrange
        coEvery { repository.deletedDataTile(id = 5) } returns mockk(relaxed = true)
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithFutureTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.OnDeleteTrip(id = 5, selectedButton = TripListDataEnum.FUTURE))
        sut.onUiIntent(TripListUiIntent.OnGetListDependsButtonType(selectedButton = TripListDataEnum.FUTURE))
        //Assert
        coVerify { repository.deletedDataTile(id = 5) }
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(createListWithFutureTrip(), sut.uiState.value.tripList)
    }

    @Test
    fun `verify visibility modal - ON`() = runTest {
        //Arrange
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPastTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.ShowTripDetails(id = 1))
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(true, sut.uiState.value.showBottomSheet)
        Assertions.assertEquals(1, sut.uiState.value.detailsId)
    }

    @Test
    fun `verify visibility modal - OFF`() = runTest {
        //Arrange
        coEvery { repository.getListDataTile() } returns flow { emit(createListWithPresentTrip()) }
        initialize()
        //Act
        sut.onUiIntent(TripListUiIntent.HideTripDetails)
        //Assert
        coVerify { repository.getListDataTile() }
        Assertions.assertEquals(false, sut.uiState.value.showBottomSheet)
        Assertions.assertEquals(null, sut.uiState.value.detailsId)
    }

    //helper methods
    private fun initialize() {
        sut = TripListViewModel(repository = repository)
    }

    private fun createListWithPastTrip(): List<Trip> =
        listOf(
            Trip(
                date = "23.11.".plus(LocalDate.now().minusYears(1).year),
                departureCity = "Warsaw",
                departureCountry = "Poland",
                departureAirport = "Chopin Warsaw",
                departureCodeAirport = "WAW",
                arrivalCity = "Cairo",
                arrivalCountry = "Egypt",
                arrivalAirport = "Cairo International Airport",
                arrivalCodeAirport = "CAI",
                id = 1
            ),
            Trip(
                date = "08.11.".plus(LocalDate.now().minusYears(1).year),
                departureCity = "Athens",
                departureCountry = "Greece",
                departureAirport = "Athens International Airport",
                departureCodeAirport = "ATH",
                arrivalCity = "Berlin",
                arrivalCountry = "Germany",
                arrivalAirport = "Berlin Brandenburg Airport",
                arrivalCodeAirport = "BER",
                id = 2
            )
        ).sortedBy { sort -> sort.date }
            .reversed()

    private fun createListWithPresentTrip(): List<Trip> =
        listOf(
            Trip(
                date = "23.11.".plus(LocalDate.now().year),
                departureCity = "Athens",
                departureCountry = "Greece",
                departureAirport = "Athens International Airport",
                departureCodeAirport = "ATH",
                arrivalCity = "Rome",
                arrivalCountry = "Italy",
                arrivalAirport = "Rome Fiumicino Airport",
                arrivalCodeAirport = "FCO",
                id = 3
            ),
            Trip(
                date = "20.11.".plus(LocalDate.now().year),
                departureCity = "Warsaw",
                departureCountry = "Poland",
                departureAirport = "Chopin Warsaw",
                departureCodeAirport = "WAW",
                arrivalCity = "Berlin",
                arrivalCountry = "Germany",
                arrivalAirport = "Berlin Brandenburg Airport",
                arrivalCodeAirport = "BER",
                id = 4
            )
        ).sortedBy { sort -> sort.date }
            .reversed()

    private fun createListWithFutureTrip(): List<Trip> =
        listOf(
            Trip(
                date = "08.11.".plus(LocalDate.now().plusYears(1).year),
                departureCity = "Paris",
                departureCountry = "France",
                departureAirport = "Charles de Gaulle Airport",
                departureCodeAirport = "CDG",
                arrivalCity = "Cairo",
                arrivalCountry = "Egypt",
                arrivalAirport = "Cairo International Airport",
                arrivalCodeAirport = "CAI",
                id = 5
            ),
            Trip(
                date = "08.10.".plus(LocalDate.now().plusYears(1).year),
                departureCity = "London",
                departureCountry = "United Kingdom",
                departureAirport = "Heathrow Airport",
                departureCodeAirport = "LHR",
                arrivalCity = "Berlin",
                arrivalCountry = "Germany",
                arrivalAirport = "Berlin Brandenburg Airport",
                arrivalCodeAirport = "BER",
                id = 6
            )
        ).sortedBy { sort -> sort.date }
            .reversed()
}