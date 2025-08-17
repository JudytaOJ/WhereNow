package com.example.wherenow.ui.app.tripdatadetails

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
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
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.navigation.Screen
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import com.example.wherenow.util.testutil.TestTag.BUTTON_TAG
import com.example.wherenow.util.testutil.TestTag.DATA_PICKER_TAG
import com.example.wherenow.util.testutil.TestTag.SEARCH_BAR_TAG
import com.example.wherenow.util.testutil.TestTag.SEARCH_TEXT_FIELD_TAG
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import util.assertTextIsDisplayed
import util.assertTextIsNotDisplayed

@OptIn(ExperimentalCoroutinesApi::class)
class TripDataDetailsScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    private val getAirportUseCase: GetAirportUseCase = mockk(relaxed = true)
    private val saveDataTileUseCase: SaveDataTileUseCase = mockk(relaxed = true)
    private val saveCityListUseCase: SaveCityListUseCase = mockk(relaxed = true)
    private val getCityListFromRepositoryUseCase: GetCityListFromRepositoryUseCase = mockk(relaxed = true)
    private val getDistanceBetweenAirportUseCase: GetDistanceBetweenAirportUseCase = mockk(relaxed = true)
    private val sendPushUseCase: SendPushUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { getAirportUseCase }
        single { saveDataTileUseCase }
        single { saveCityListUseCase }
        single { getCityListFromRepositoryUseCase }
        single { getDistanceBetweenAirportUseCase }
        single { sendPushUseCase }
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
    fun verification_of_static_texts_on_the_screen() {
        initialize()

        // Checked elements display on screen
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_title)
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_date)
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_city_label))
        composeRule.assertTextIsDisplayed(R.string.test_button_text_add)

        // Checked elements not display on screen
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_departure)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_arrival)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_code_label)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_country_label)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_airport_name_label)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_distance_label)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_calendar_select)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_calendar_cancel)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_city_input_validation)
        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_miles)
    }

    @Test
    fun verification_of_validation_when_first_and_second_dropdown_is_empty() {
        initialize()

        composeRule.onNodeWithTag(FIRST_DROPDOWN)
            .assertIsDisplayed()
        composeRule.onNodeWithTag(SECOND_DROPDOWN)
            .assertIsDisplayed()

        //Clicked button
        composeRule.onNodeWithTag(BUTTON_TAG)
            .assertIsDisplayed()
            .assertTextEquals(composeRule.activity.getString(R.string.test_button_text_add))
            .assertHasClickAction()
            .performClick()

        //Checked validations
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_city_input_validation))
    }

    @Test
    fun verification_of_state_texts_when_first_dropdown_is_not_empty() {
        initialize()

        composeRule.onNodeWithTag(FIRST_DROPDOWN)
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag(SEARCH_BAR_TAG)
            .assertIsEnabled()
            .performClick()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG)
            .performTextInput(MIAMI)
        composeRule.onAllNodesWithText(MIAMI)
            .onLast()
            .assertIsDisplayed()
            .performClick()

        //Checked display read-only inputs
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_code_label)
        composeRule.onNodeWithText("MIA").isDisplayed()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_country_label)
        composeRule.onNodeWithText("United States").isDisplayed()
        composeRule.onNodeWithTag(BUTTON_TAG).performScrollTo()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_airport_name_label)
        composeRule.onNodeWithText("Miami International Airport").isDisplayed()

        //Checked validation in second dropdown
        composeRule.onNodeWithTag(BUTTON_TAG).performClick()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_city_input_validation)
    }

    @Test
    fun check_reopening_the_modal_with_the_city_list() {
        initialize()

        composeRule.onNodeWithTag(FIRST_DROPDOWN).performClick()
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performClick()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG).performTextInput(MIAMI)
        composeRule.onAllNodesWithText(MIAMI)
            .onLast()
            .performClick()

        composeRule.assertTextIsDisplayed(R.string.test_trip_details_code_label)
        composeRule.onNodeWithText("MIA").isDisplayed()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_country_label)
        composeRule.onNodeWithText("United States").isDisplayed()
        composeRule.onNodeWithTag(BUTTON_TAG).performScrollTo()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_airport_name_label)
        composeRule.onNodeWithText("Miami International Airport").isDisplayed()

        composeRule.onNodeWithTag(FIRST_DROPDOWN).performClick()
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performClick()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG).performTextClearance()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG).performTextInput(AKRON)
        composeRule.onAllNodesWithText(AKRON)
            .onLast()
            .performClick()

        //Checked re-opening data
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_code_label)
        composeRule.onNodeWithText("CAK").isDisplayed()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_country_label)
        composeRule.onNodeWithText("United States").isDisplayed()
        composeRule.onNodeWithTag(BUTTON_TAG).performScrollTo()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_airport_name_label)
        composeRule.onNodeWithText("Akron-Canton Airport").isDisplayed()
    }

    @Test
    fun verification_of_state_texts_when_second_dropdown_is_not_empty() {
        initialize()

        composeRule.onNodeWithTag(SECOND_DROPDOWN)
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag(SEARCH_BAR_TAG)
            .assertIsEnabled()
            .performClick()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG)
            .performTextInput(AKRON)
        composeRule.onAllNodesWithText(AKRON)
            .onLast()
            .assertIsDisplayed()
            .performClick()

        //Checked display read-only inputs
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_code_label)
        composeRule.onNodeWithText("CAK").isDisplayed()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_country_label)
        composeRule.onNodeWithText("United States").isDisplayed()
        composeRule.onNodeWithTag(BUTTON_TAG).performScrollTo()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_airport_name_label)
        composeRule.onNodeWithText("Akron-Canton Airport").isDisplayed()

        //Checked validation in first dropdown
        composeRule.onNodeWithTag(BUTTON_TAG).performClick()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_city_input_validation)
    }

    @Test
    fun check_if_selecting_a_city_clears_the_validation() {
        initialize()
        coEvery { getDistanceBetweenAirportUseCase.invoke(any()) } returns createDistanceList()

        composeRule.onNodeWithTag(BUTTON_TAG)
            .assertIsDisplayed()
            .assertTextEquals(composeRule.activity.getString(R.string.test_button_text_add))
            .assertHasClickAction()
            .performClick()
        composeRule.onAllNodesWithText(composeRule.activity.getString(R.string.test_trip_details_city_input_validation))

        composeRule.onNodeWithTag(FIRST_DROPDOWN).performClick()
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performClick()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG).performTextInput(AKRON)
        composeRule.onAllNodesWithText(AKRON)
            .onLast()
            .performClick()

        composeRule.onNodeWithTag(SECOND_DROPDOWN).performClick()
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performClick()
        composeRule.onNodeWithTag(SEARCH_TEXT_FIELD_TAG).performTextInput(MIAMI)
        composeRule.onAllNodesWithText(MIAMI)
            .onLast()
            .performClick()

        composeRule.assertTextIsNotDisplayed(R.string.test_trip_details_city_input_validation)

        //Checked distance input is displayed
        composeRule.onNodeWithTag(BUTTON_TAG).performScrollTo()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_distance_label)
        composeRule.onNodeWithText(MILES)
    }

    @Test
    fun verification_action_when_arrow_back_clicked() {
        initialize()

        composeRule.onNodeWithTag(BACK_ICON_TAG)
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun verification_calendar_text_buttons() {
        initialize()

        composeRule.onNodeWithTag(DATA_PICKER_TAG)
            .assertHasClickAction()
            .performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_trip_details_calendar_select), useUnmergedTree = false)
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_trip_details_calendar_cancel), useUnmergedTree = false)
    }

    //helper method
    private fun initialize() {
        setup()

        coEvery { getCityListFromRepositoryUseCase.invoke() } returns createCityList()

        composeRule.setContent {
            navController = rememberNavController()
            WhereNowTheme {
                NavHost(
                    navController = navController,
                    onCloseApp = {},
                    openFile = {}
                )
            }
            navController.navigate(Screen.TripDetails.route)
        }
    }

    private fun createCityList(): List<AttributesDto> = listOf(
        AttributesDto(
            attributes = DataItemDto(
                city = AKRON,
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
                city = MIAMI,
                country = "United States",
                iata = "MIA",
                icao = "KMIA",
                name = "Miami International Airport"
            ),
            id = "3",
            type = "3"
        )
    )

    private fun createDistanceList(): List<DistanceBetweenAirportDto> = listOf(
        DistanceBetweenAirportDto(
            distanceAirportList = DistanceDto(
                attributes = DistanceAttributesDto(
                    fromAirport = DataItemDto(
                        city = AKRON,
                        country = "United States",
                        iata = "CAK",
                        icao = "KCAK",
                        name = "Akron-Canton Airport"
                    ),
                    toAirport = DataItemDto(
                        city = MIAMI,
                        country = "United States",
                        iata = "MIA",
                        icao = "KMIA",
                        name = "Miami International Airport"
                    ),
                    miles = MILES
                ),
                id = "1",
                type = "1"
            )
        )
    )

    //helper const
    companion object {
        const val FIRST_DROPDOWN = "firstDropdown"
        const val SECOND_DROPDOWN = "secondDropdown"
        const val MIAMI = "Miami"
        const val AKRON = "Akron"
        const val MILES = "1000"
    }
}