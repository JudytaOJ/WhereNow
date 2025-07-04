package com.example.wherenow.ui.app.triptiledetails

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import com.example.wherenow.util.testutil.TestTag.FLIGHT_TILE_TAG
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import util.assertTextIsDisplayed
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class TripTileDetailsScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    private val getListDataTileUseCase: GetListDataTileUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { getListDataTileUseCase }
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
    fun verification_action_when_arrow_back_clicked() {
        initialize()

        composeRule.onNodeWithTag(BACK_ICON_TAG)
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun verification_of_static_texts_on_the_screen() {
        initialize()

        // Checked elements display on screen
        composeRule.assertTextIsDisplayed(R.string.test_app_name)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_tile_list_name_flight_details)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_supported_text_flight_details)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_tile_list_name_important_notes)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_supported_text_important_notes)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_tile_list_name_add_file)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_supported_text_list_name_add_file)
    }

    @Test
    fun checked_details_flight_on_modal() {
        initialize()

        composeRule.onAllNodesWithTag(FLIGHT_TILE_TAG).onFirst().performClick()

        //Departure
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_departure)
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_city_label)).onFirst().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_country_label)).onFirst().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_date)).onFirst().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_code_label)).onFirst().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_airport_name_label)).onFirst().isDisplayed()

        //Arrival
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_arrival)
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_city_label)).onLast().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_country_label)).onLast().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_date)).onLast().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_code_label)).onLast().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_airport_name_label)).onLast().isDisplayed()

        //Distance
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_distance_label)).onLast().isDisplayed()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_distance_between_cites)).onLast().isDisplayed()
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
            navController.navigate(TILE_DETAILS_ROUTE)
        }
    }
}