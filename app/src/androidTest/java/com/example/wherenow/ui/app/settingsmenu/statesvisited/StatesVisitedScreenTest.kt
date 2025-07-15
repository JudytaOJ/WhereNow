package com.example.wherenow.ui.app.settingsmenu.statesvisited

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.data.usecases.SaveStatesVisitedUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
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
import util.waitFewSeconds

@OptIn(ExperimentalCoroutinesApi::class)
class StatedVisitedScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    private val saveStatesVisitedUseCase: SaveStatesVisitedUseCase = mockk(relaxed = true)
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { saveStatesVisitedUseCase }
        single { getStatesVisitedUseCase }
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
        composeRule.onNodeWithText(ALABAMA).assertIsDisplayed()
        composeRule.onNodeWithText(ALASKA).assertIsDisplayed()
        composeRule.onNodeWithText(HAWAII).assertIsDisplayed()
        composeRule.onNodeWithText(WYOMING).assertIsDisplayed()
        composeRule.assertTextIsDisplayed(R.string.test_states_visited_title)
    }

    @Test
    fun verification_of_the_ability_to_check_checkboxes_and_display_a_congratulatory_message() {
        initialize()

        // Checked checkboxes clickable
        (1..4).forEach { id ->
            composeRule.onNodeWithTag("Checkbox_$id")
                .assertHasClickAction()
                .performClick()
        }
        composeRule.waitForIdle()

        // Checked the display of text when all checkboxes are selected
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_state_congratulations))

        // Check if the animation lasts a few seconds
        composeRule.waitFewSeconds(3000)

        // Checked the not display of text when not all checkboxes are selected
        composeRule.onNodeWithTag("Checkbox_4").performClick().assertIsOff()
        composeRule.assertTextIsNotDisplayed(R.string.test_state_congratulations)
    }

    @Test
    fun not_all_checkboxes_checked_does_not_show_congratulatory_message() {
        initialize()

        (1..3).forEach { id ->
            composeRule.onNodeWithTag("Checkbox_$id").performClick()
        }

        // Checked the not display of text when not all checkboxes are selected
        composeRule.assertTextIsNotDisplayed(R.string.test_state_congratulations)
    }

    @Test
    fun back_button_is_clickable() {
        initialize()

        composeRule.onNodeWithTag(BACK_ICON_TAG)
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun screen_handles_empty_state_list() {
        initialize(isNotEmptyList = false)

        (1..4).forEach { id ->
            composeRule.onNodeWithTag("Checkbox_$id").assertDoesNotExist()
        }
    }

    @Test
    fun screen_loads_initial_state_list_correctly() {
        initialize()

        (1..4).forEach { id ->
            composeRule.onNodeWithTag("Checkbox_$id").assertIsOff()
        }
    }

    @Test
    fun checked_item_has_strikethrough_text_style() {
        initialize()

        (1..3).forEach { id ->
            composeRule.onNodeWithTag("Checkbox_$id").performClick().assertIsOn()
            composeRule.onNodeWithTag("Strikethrough_$id", useUnmergedTree = true).assertExists()
        }

        composeRule.onNodeWithTag("Checkbox_4").assertIsOff()
        composeRule.onNodeWithTag("Strikethrough_4").assertDoesNotExist()
        composeRule.onNodeWithTag("Normal_4", useUnmergedTree = true).assertExists()
    }

    //helper method
    private fun initialize(
        isNotEmptyList: Boolean = true
    ) {
        setup()
        if (isNotEmptyList) {
            coEvery { getStatesVisitedUseCase(any()) } returns createFakeStatesList()
        } else {
            coEvery { getStatesVisitedUseCase(any()) } returns emptyList()
        }

        composeRule.setContent {
            navController = rememberNavController()
            WhereNowTheme {
                NavHost(
                    navController = navController,
                    onCloseApp = {},
                    openFile = {}
                )
            }
            navController.navigate(STATES_VISITED)
        }
    }

    private fun createFakeStatesList(): List<StateItem> =
        listOf(
            StateItem(
                text = ALABAMA,
                imageRes = R.drawable.flag_of_alabama,
                id = 1,
                isChecked = false
            ),
            StateItem(
                text = ALASKA,
                imageRes = R.drawable.flag_of_alaska,
                id = 2,
                isChecked = false
            ),
            StateItem(
                text = HAWAII,
                imageRes = R.drawable.flag_of_hawaii,
                id = 3,
                isChecked = false
            ),
            StateItem(
                text = WYOMING,
                imageRes = R.drawable.flag_of_wyoming,
                id = 4,
                isChecked = false
            )
        )

    //helper const
    companion object {
        const val ALASKA = "Alaska"
        const val ALABAMA = "Alabama"
        const val HAWAII = "Hawaii"
        const val WYOMING = "Wyoming"
    }
}