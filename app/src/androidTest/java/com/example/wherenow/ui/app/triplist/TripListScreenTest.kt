package com.example.wherenow.ui.app.triplist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
import com.example.wherenow.data.usecases.CancelPushUseCase
import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetActuallyTripListUseCase
import com.example.wherenow.data.usecases.GetFutureTripListUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.repository.models.TripListItemData
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import com.example.wherenow.util.testutil.TestTag.DELETE_TILE
import com.example.wherenow.util.testutil.TestTag.FLOATING_ACTION_BUTTON_TAG
import com.example.wherenow.util.testutil.TestTag.LOTTIE_ANIMATION_TAG
import com.example.wherenow.util.testutil.TestTag.MENU_TAG
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import util.assertTextIsDisplayed
import java.time.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)
class TripListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    private val deleteTileOnListUseCase: DeleteTileOnListUseCase = mockk(relaxed = true)
    private val getPastTripListUseCase: GetPastTripListUseCase = mockk(relaxed = true)
    private val getActuallyTripListUseCase: GetActuallyTripListUseCase = mockk(relaxed = true)
    private val getFutureTripListUseCase: GetFutureTripListUseCase = mockk(relaxed = true)
    private val cancelPushUseCase: CancelPushUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { deleteTileOnListUseCase }
        single { getPastTripListUseCase }
        single { getActuallyTripListUseCase }
        single { getFutureTripListUseCase }
        single { cancelPushUseCase }
    }

    @BeforeTest
    fun setup() {
        unloadKoinModules(testKoinModule)
        loadKoinModules(testKoinModule)
    }

    @AfterTest
    fun tearDown() {
        unloadKoinModules(testKoinModule)
    }

    @Test
    fun verification_action_when_menu_clicked() {
        initialize()

        composeRule.onNodeWithTag(MENU_TAG)
            .assertHasClickAction()
            .performClick()

        composeRule.assertTextIsDisplayed(R.string.test_trip_list_navigation_drawer_title)
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_navigation_drawer_states)
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_navigation_drawer_close_app)
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_navigation_drawer_dark_theme)
    }

    @Test
    fun verifying_drawer_open_and_close_on_menu_click() {
        initialize()
        val menuNode = composeRule.onNodeWithTag(MENU_TAG)

        menuNode.performClick()
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_navigation_drawer_states)
        menuNode.performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_trip_list_navigation_drawer_close_app)).isNotDisplayed()
    }

    @Test
    fun verifying_static_texts_on_the_screen() {
        initialize()

        composeRule.assertTextIsDisplayed(R.string.test_app_name)
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_empty_state)
        composeRule.assertTextIsDisplayed(R.string.test_segmented_button_past)
        composeRule.assertTextIsDisplayed(R.string.test_segmented_button_present)
        composeRule.assertTextIsDisplayed(R.string.test_segmented_button_future)

        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).assertHasClickAction().performClick()
    }

    @Test
    fun verifying_past_present_and_future_list_when_lists_are_empty() {
        initialize()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_past))
            .performClick()
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_empty_state)
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).assertHasClickAction()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_present))
            .performClick()
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_empty_state)
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).assertHasClickAction()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_future))
            .performClick()
        composeRule.assertTextIsDisplayed(R.string.test_trip_list_empty_state)
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).assertHasClickAction()
    }

    @Test
    fun verifying_past_list_when_list_is_not_empty() {
        initialize()
        coEvery { getPastTripListUseCase.invoke() } returns createPastList()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_past))
            .performClick()
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).assertHasClickAction()

        //First tile
        composeRule.onNodeWithText("New york").isDisplayed()
        composeRule.onNodeWithText("United States").isDisplayed()
        composeRule.onAllNodesWithText("Departure date")[0].isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_card_travel_complete))[0].isDisplayed()

        //Second tile
        composeRule.onNodeWithText("San Francisco").isDisplayed()
        composeRule.onNodeWithText("United States").isDisplayed()
        composeRule.onAllNodesWithText("Departure date")[1].isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_card_travel_complete))[1].isDisplayed()
    }

    @Test
    fun verifying_onDeleteTrip_removes_tile() = runTest {
        coEvery { getPastTripListUseCase.invoke() } returns createPastList()
        coEvery { deleteTileOnListUseCase.invoke(any()) } just Runs
        coEvery { cancelPushUseCase.invoke(any()) } just Runs

        initialize()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_past))
            .performClick()
        composeRule.onAllNodesWithTag(DELETE_TILE)[0].performClick()

        coVerify { cancelPushUseCase.invoke(1) }
        coVerify { deleteTileOnListUseCase.invoke(1) }

        composeRule.onNodeWithText("New york").isNotDisplayed()
        composeRule.waitUntilDoesNotExist(hasText("New York"))
    }

    @Test
    fun verifying_all_trip_data_enum_values_update_state() {
        coEvery { getPastTripListUseCase.invoke() } returns createPastList()
        coEvery { getActuallyTripListUseCase.invoke() } returns createPresentList()
        coEvery { getFutureTripListUseCase.invoke() } returns createFutureList()

        initialize()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_past)).performClick()
        composeRule.onNodeWithText("San Francisco").isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_card_travel_complete))[1].isDisplayed()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_present)).performClick()
        composeRule.onNodeWithText("New york").isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_card_travel_complete_today))[1].isDisplayed()

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_segmented_button_future)).performClick()
        composeRule.onNodeWithText("New york").isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_card_travel_now))[1].isDisplayed()
    }

    @Test
    fun verifying_lottie_animation_is_not_playing_in_test_mode() {
        initialize()

        composeRule
            .onNodeWithTag(LOTTIE_ANIMATION_TAG)
            .assertExists()

        composeRule.mainClock.advanceTimeBy(3000)
        composeRule.waitForIdle()

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.test_trip_list_empty_state)
        ).assertIsDisplayed()
    }

    //helper method
    private fun initialize() {
        setup()

        composeRule.setContent {
            navController = rememberNavController()
            WhereNowTheme {
                NavHost(
                    navController = navController,
                    onCloseApp = {},
                    openFile = {}
                )
            }
            navController.navigate(TRIP_LIST_ROUTE)
        }
    }

    private fun createPastList() = listOf(
        TripListItemData(
            date = convertLocalDateToTimestampUTC(LocalDate.now().minusMonths(2)),
            image = WhereNowDetailsTileImageType.US_HAWAII.icon,
            departureCity = "New York",
            departureCountry = "United States",
            departureAirport = "John F. Kennedy International Airport",
            departureCodeAirport = "JFK",
            arrivalCity = "Los Angeles",
            arrivalCountry = "United States",
            arrivalAirport = "Los Angeles International Airport",
            arrivalCodeAirport = "LAX",
            distance = "1234",
            id = 1
        ),
        TripListItemData(
            date = convertLocalDateToTimestampUTC(LocalDate.now().minusMonths(2)),
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
    )

    private fun createPresentList() = listOf(
        TripListItemData(
            date = convertLocalDateToTimestampUTC(LocalDate.now()),
            image = WhereNowDetailsTileImageType.US_HAWAII.icon,
            departureCity = "New York",
            departureCountry = "United States",
            departureAirport = "John F. Kennedy International Airport",
            departureCodeAirport = "JFK",
            arrivalCity = "Los Angeles",
            arrivalCountry = "United States",
            arrivalAirport = "Los Angeles International Airport",
            arrivalCodeAirport = "LAX",
            distance = "1234",
            id = 1
        )
    )

    private fun createFutureList() = listOf(
        TripListItemData(
            date = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(6)),
            image = WhereNowDetailsTileImageType.US_HAWAII.icon,
            departureCity = "New York",
            departureCountry = "United States",
            departureAirport = "John F. Kennedy International Airport",
            departureCodeAirport = "JFK",
            arrivalCity = "Los Angeles",
            arrivalCountry = "United States",
            arrivalAirport = "Los Angeles International Airport",
            arrivalCodeAirport = "LAX",
            distance = "1234",
            id = 1
        )
    )
}