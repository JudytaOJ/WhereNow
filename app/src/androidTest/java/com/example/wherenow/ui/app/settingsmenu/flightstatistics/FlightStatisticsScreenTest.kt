package com.example.wherenow.ui.app.settingsmenu.flightstatistics

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
import com.example.wherenow.data.usecases.GetFeaturesStatisticsUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.navigation.Screen
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesProvider
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.StringUtils
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.unloadKoinModules
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
class FlightStatisticsScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    private val getFeaturesStatisticsUseCase: GetFeaturesStatisticsUseCase = mockk(relaxed = true)
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase = mockk(relaxed = true)
    private val statesProvider: StatesProvider = mockk(relaxed = true)
    private val getPastTripListUseCase: GetPastTripListUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { getFeaturesStatisticsUseCase }
        single { getStatesVisitedUseCase }
        single { statesProvider }
        single { getPastTripListUseCase }
    }

    @Before
    fun setup() {
        unloadKoinModules(testKoinModule)
        loadKoinModules(testKoinModule)
    }

    @After
    fun tearDown() {
        unloadKoinModules(testKoinModule)
    }

    @Test
    fun should_display_most_frequent_route() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(MOST_FREQUENT_ROUTE)
            .assertTextContains("Seattle-New York", substring = true)
    }

    @Test
    fun should_display_longest_and_shortest_flight() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(LONGEST_SHORTEST)
            .assertTextContains("300/100", substring = true)
    }

    @Test
    fun should_display_flights_per_month() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(FLIGHTS_THIS_MONTH)
            .assertExists()
    }

    @Test
    fun should_display_top_arrival_and_departure() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TOP_ARRIVAL).assertExists()
        composeRule.onNodeWithTag(TOP_DEPARTURE).assertExists()
    }

    @Test
    fun should_display_usa_map() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(USA_MAP)
            .assertIsDisplayed()
    }

    @Test
    fun should_handle_empty_state() {
        initialize(empty = true)
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(STATISTICS_BOX).assertExists()
        composeRule.onNodeWithTag(USA_MAP).assertExists()
    }

    @Test
    fun should_display_all_sections() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(YOUR_FLIGHTS_SECTION).assertIsDisplayed()
        composeRule.onNodeWithTag(YOUR_ACTIVITY_SECTION).assertIsDisplayed()
    }

    @Test
    fun should_display_states_counter() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("2/", substring = true).assertIsDisplayed()
    }

    @Test
    fun should_display_total_miles() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TOTAL_DISTANCE).assertIsDisplayed()
    }

    @Test
    fun back_button_is_clickable() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(BACK_ICON_TAG)
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun verification_of_static_texts_on_the_screen() {
        initialize()
        composeRule.waitForIdle()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_states_visited_title)).assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_statistics_visited_states_header)).assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_statistics_your_flight_header)).assertIsDisplayed()
    }

    private fun initialize(empty: Boolean = false) {
        if (empty) {
            coEvery { getPastTripListUseCase.invoke() } returns emptyList()
            coEvery { getFeaturesStatisticsUseCase.invoke() } returns emptyList()
            coEvery { getStatesVisitedUseCase.invoke(any()) } returns emptyList()
            every { statesProvider.provide() } returns emptyList()
        } else {
            coEvery { getPastTripListUseCase.invoke() } returns createTrips()
            coEvery { getFeaturesStatisticsUseCase.invoke() } returns emptyList()
            coEvery { getStatesVisitedUseCase.invoke(any()) } returns createVisitedStates()
            every { statesProvider.provide() } returns createBaseStates()
        }

        composeRule.setContent {
            navController = rememberNavController()
            WhereNowTheme {
                NavHost(
                    navController = navController,
                    onCloseApp = {},
                    openFile = {},
                    calendarApp = {}
                )
            }
            navController.navigate(Screen.FlightStatistics.route)
        }
    }

    private fun createTrips(): List<TripListItemData> =
        listOf(
            createTrip("New York", "Los Angeles", 100),
            createTrip("Seattle", "New York", 300),
            createTrip("Seattle", "New York", 300),
        )

    private fun createTrip(
        departure: String,
        arrival: String,
        distance: Int
    ) = TripListItemData(
        departureCity = departure,
        arrivalCity = arrival,
        distance = distance.toString(),
        date = System.currentTimeMillis(),
        image = 0,
        departureCountry = StringUtils.EMPTY,
        departureAirport = StringUtils.EMPTY,
        departureCodeAirport = StringUtils.EMPTY,
        arrivalAirport = StringUtils.EMPTY,
        arrivalCountry = StringUtils.EMPTY,
        arrivalCodeAirport = StringUtils.EMPTY,
        id = 0
    )

    private fun createVisitedStates(): List<StateItem> =
        listOf(
            StateItem("Alabama", 0, 1, true),
            StateItem("Alaska", 0, 2, false),
            StateItem("Hawaii", 0, 3, true)
        )

    private fun createBaseStates(): List<StateItem> =
        listOf(
            StateItem("Alabama", 0, 1, false),
            StateItem("Alaska", 0, 2, false),
            StateItem("Hawaii", 0, 3, false)
        )

    companion object {
        const val TOTAL_DISTANCE = "TOTAL_DISTANCE"
        const val YOUR_ACTIVITY_SECTION = "YOUR_ACTIVITY_SECTION"
        const val YOUR_FLIGHTS_SECTION = "YOUR_FLIGHTS_SECTION"
        const val USA_MAP = "USA_MAP"
        const val STATISTICS_BOX = "STATISTICS_BOX"
        const val TOP_DEPARTURE = "TOP_DEPARTURE"
        const val TOP_ARRIVAL = "TOP_ARRIVAL"
        const val FLIGHTS_THIS_MONTH = "FLIGHTS_THIS_MONTH"
        const val LONGEST_SHORTEST = "LONGEST_SHORTEST"
        const val MOST_FREQUENT_ROUTE = "MOST_FREQUENT_ROUTE"
    }
}