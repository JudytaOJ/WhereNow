@file:OptIn(ExperimentalEncodingApi::class)

package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.R
import com.example.wherenow.data.usecases.SaveImportantNoteUseCase
import com.example.wherenow.data.usecases.UpdateImportantNoteUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.navigation.Screen
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.StringUtils
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import com.example.wherenow.util.testutil.TestTag.BLANK_NOTE_DESCRIPTION_TAG
import com.example.wherenow.util.testutil.TestTag.BLANK_NOTE_TITLE_TAG
import io.mockk.coVerify
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
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalCoroutinesApi::class)
class BlankNoteScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val saveImportantNoteUseCase: SaveImportantNoteUseCase = mockk(relaxed = true)
    private val updateImportantNoteUseCase: UpdateImportantNoteUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { saveImportantNoteUseCase }
        single { updateImportantNoteUseCase }
        viewModel {
            BlankNoteViewModel(
                savedStateHandle = savedStateHandle,
                saveImportantNoteUseCase = get(),
                updateImportantNoteUseCase = get()
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

        composeRule.onNodeWithTag(BACK_ICON_TAG)
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun default_title_and_description_fields_are_displayed() {
        initialize()

        //Checked text in toolbar
        composeRule.assertTextIsDisplayed(R.string.test_trip_details_tile_list_name_important_notes)
        //Checked placeholders
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_blank_note_title)).isDisplayed()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_blank_note_description)).isDisplayed()
        //Checked text button
        composeRule.assertTextIsDisplayed(R.string.test_button_text_add)
    }

    @Test
    fun user_can_type_title_and_description() {
        val newTitle = "New Title"
        val newDescription = "This is a new description"

        initialize()

        //clean inputs and add new title and description
        composeRule.onNodeWithTag(BLANK_NOTE_TITLE_TAG).performTextClearance()
        composeRule.onNodeWithTag(BLANK_NOTE_TITLE_TAG).performTextInput(newTitle)
        composeRule.onNodeWithTag(BLANK_NOTE_DESCRIPTION_TAG).performTextClearance()
        composeRule.onNodeWithTag(BLANK_NOTE_DESCRIPTION_TAG).performTextInput(newDescription)
        composeRule.onNodeWithText(newTitle).assertIsDisplayed()
        composeRule.onNodeWithText(newDescription).assertIsDisplayed()
    }

    @Test
    fun custom_title_and_description_fields_are_displayed() {
        val customTitle = Base64.encode("Custom title".toByteArray())
        val customDescription = Base64.encode("Custom description - Lorem ipsum".toByteArray())
        initialize(
            title = customTitle,
            description = customDescription,
            id = 4
        )

        //Checked title and description
        composeRule.onNodeWithText(customTitle).isDisplayed()
        composeRule.onNodeWithText(LoremIpsum().values.joinToString()).isDisplayed()
        //Checked text and action button
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.test_button_text_edit)).performClick()


        //Checked call service
        coVerify(exactly = 1) { saveImportantNoteUseCase(any()) }
    }

    //helper method
    private fun initialize(
        title: String = TITLE,
        description: String = DESCRIPTION,
        id: Int = ID
    ) {
        every { savedStateHandle.get<String>(TripTileDetailsTag.TITLE_EDIT_NOTE) } returns title
        every { savedStateHandle.get<String>(TripTileDetailsTag.DESCRIPTION_EDIT_NOTE) } returns description
        every { savedStateHandle.get<Int>(TripTileDetailsTag.ID_NOTE) } returns id
        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID

        setup()

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
            navController.navigate(
                Screen.BlankNote.passArgs(
                    titleEditNote = StringUtils.EMPTY,
                    descriptionEditNote = StringUtils.EMPTY,
                    idNote = StringUtils.EMPTY,
                    tripId = "1"
                )
            )
        }
    }

    //const val helper
    companion object {
        const val TITLE = "Title"
        const val DESCRIPTION = "Description"
        const val ID = 0
        const val TRIP_ID = 5
    }
}