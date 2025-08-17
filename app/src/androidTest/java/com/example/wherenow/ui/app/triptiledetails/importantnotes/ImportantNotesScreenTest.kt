package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
import com.example.wherenow.data.usecases.DeleteImportantNoteUseCase
import com.example.wherenow.data.usecases.GetImportantNotesListUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.navigation.Screen
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import com.example.wherenow.util.testutil.TestTag.FLOATING_ACTION_BUTTON_TAG
import com.example.wherenow.util.testutil.TestTag.LOTTIE_ANIMATION_TAG
import com.example.wherenow.util.testutil.TestTag.NOTES_TILE_DELETE_TAG
import com.example.wherenow.util.testutil.TestTag.NOTES_TILE_TAG
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import util.assertTextIsDisplayed
import util.assertTextIsNotDisplayed
import util.waitFewSeconds

@OptIn(ExperimentalCoroutinesApi::class)
class ImportantNotesScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController
    private var currentNotesList = mutableListOf<ImportantNoteItemData>()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getImportantNotesListUseCase: GetImportantNotesListUseCase = mockk(relaxed = true)
    private val deleteImportantNoteUseCase: DeleteImportantNoteUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { getImportantNotesListUseCase }
        single { deleteImportantNoteUseCase }
        viewModel {
            ImportantNotesViewModel(
                savedStateHandle = savedStateHandle,
                getImportantNotesListUseCase = get(),
                deleteImportantNoteUseCase = get()
            )
        }
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
    fun verification_action_when_arrow_back_clicked() {
        initialize()

        composeRule.onNodeWithTag(BACK_ICON_TAG).assertHasClickAction()
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_tile_list_name_important_notes)
        composeRule.activity.onBackPressedDispatcher.onBackPressed()
    }

    @Test
    fun check_lottie_animation_and_description_is_visible_when_list_is_empty() {
        initialize(isEmptyList = true)

        composeRule.onNodeWithTag(LOTTIE_ANIMATION_TAG).assertIsDisplayed()
        composeRule.waitFewSeconds(3000)
        composeRule.assertTextIsDisplayed(R.string.test_important_notes_empty_state_text)
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun verification_how_many_notes_is_display() {
        initialize()

        //Notes 1
        composeRule.onAllNodesWithTag(NOTES_TILE_TAG)[0]
            .assertTextEquals("Title1", "Description1")
            .assertIsDisplayed()
            .assertHasClickAction()
        //Notes 2
        composeRule.onAllNodesWithTag(NOTES_TILE_TAG)[1]
            .assertTextEquals("Title2", "Description2")
            .assertIsDisplayed()
            .assertHasClickAction()
        //Notes 3
        composeRule.onAllNodesWithTag(NOTES_TILE_TAG)[2]
            .assertTextEquals("Title3", "Description3")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG)
            .assertIsDisplayed()
            .assertHasClickAction()

        composeRule.onNodeWithTag(LOTTIE_ANIMATION_TAG).assertIsNotDisplayed()
        composeRule.assertTextIsNotDisplayed(R.string.test_important_notes_empty_state_text)
    }

    @Test
    fun check_how_many_notes_are_displayed_after_removing_the_first() {
        initialize()

        composeRule.onAllNodesWithTag(NOTES_TILE_TAG)[0]
            .assertTextEquals("Title1", "Description1")
            .assertIsDisplayed()

        composeRule.onAllNodesWithTag(NOTES_TILE_DELETE_TAG)[0].performClick()

        composeRule.onNodeWithText("Title1").assertIsNotDisplayed()
        composeRule.onNodeWithText("Description1").assertIsNotDisplayed()
    }

    @Test
    fun removing_all_notes_shows_empty_state() {
        initialize()

        currentNotesList.toList().forEach { note ->
            composeRule.onAllNodesWithTag(NOTES_TILE_DELETE_TAG)[0].performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag(LOTTIE_ANIMATION_TAG).assertIsDisplayed()
        composeRule.assertTextIsDisplayed(R.string.important_notes_empty_state_text)
    }

    //helper method
    private fun initialize(
        isEmptyList: Boolean = false
    ) {
        currentNotesList = if (isEmptyList) {
            mutableListOf()
        } else {
            createImportantNoteItemData().toMutableList()
        }

        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID
        coEvery { getImportantNotesListUseCase.invoke() } answers {
            currentNotesList.toList()
        }
        coEvery { deleteImportantNoteUseCase.invoke(any()) } answers {
            val idToDelete = it.invocation.args[0] as Int
            currentNotesList.removeAll { note -> note.id == idToDelete }
            Unit
        }

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
            navController.navigate(Screen.ImportantNotes.passArgs(1))
        }
    }

    private fun createImportantNoteItemData() = listOf(
        ImportantNoteItemData(
            title = "Title1",
            description = "Description1",
            id = 1,
            tripId = TRIP_ID
        ),
        ImportantNoteItemData(
            title = "Title2",
            description = "Description2",
            id = 2,
            tripId = TRIP_ID
        ),
        ImportantNoteItemData(
            title = "Title3",
            description = "Description3",
            id = 3,
            tripId = TRIP_ID
        )
    )

    //const val helper
    companion object {
        const val TRIP_ID = 8
    }
}