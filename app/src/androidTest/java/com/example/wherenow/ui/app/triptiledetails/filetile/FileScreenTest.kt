package com.example.wherenow.ui.app.triptiledetails.filetile

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
import com.example.wherenow.data.usecases.DeleteFileUseCase
import com.example.wherenow.data.usecases.GetFileNameUseCase
import com.example.wherenow.data.usecases.GetFilesListUseCase
import com.example.wherenow.data.usecases.SaveFileUseCase
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import com.example.wherenow.util.testutil.TestTag.DELETE_FILE
import com.example.wherenow.util.testutil.TestTag.FILE_ITEM
import com.example.wherenow.util.testutil.TestTag.FLOATING_ACTION_BUTTON_TAG
import com.example.wherenow.util.testutil.TestTag.LOTTIE_ANIMATION_TAG
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import util.assertTextIsDisplayed
import util.waitFewSeconds
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class FileScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController
    private var currentFileList = MutableStateFlow<List<FileData>>(emptyList())

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getFilesListUseCase: GetFilesListUseCase = mockk(relaxed = true)
    private val saveFileUseCase: SaveFileUseCase = mockk(relaxed = true)
    private val deleteFileUseCase: DeleteFileUseCase = mockk(relaxed = true)
    private val getFileNameUseCase: GetFileNameUseCase = mockk(relaxed = true)

    private val testKoinModule = module {
        single { getFilesListUseCase }
        single { saveFileUseCase }
        single { deleteFileUseCase }
        single { getFileNameUseCase }
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

        composeRule.assertTextIsDisplayed(R.string.test_file_title)
        composeRule.onNodeWithTag(BACK_ICON_TAG)
            .assertHasClickAction()
            .performClick()
    }

    @Test
    fun check_lottie_animation_and_description_is_visible_when_list_is_empty() {
        initialize(isEmptyList = true)

        composeRule.onNodeWithTag(LOTTIE_ANIMATION_TAG).assertIsDisplayed()
        composeRule.waitFewSeconds(3000)
        composeRule.assertTextIsDisplayed(R.string.test_file_empty_list)
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun check_alert_is_visible() {
        initialize()

        composeRule.assertTextIsDisplayed(R.string.test_file_alert)
        composeRule.onNodeWithTag(FLOATING_ACTION_BUTTON_TAG)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun check_item_and_display_name() {
        initialize()

        composeRule.onNodeWithText("File1").assertIsDisplayed()
        composeRule.onNodeWithText("File2").assertIsDisplayed()
        composeRule.onAllNodesWithTag(DELETE_FILE)[0].assertIsDisplayed().assertHasClickAction()
        composeRule.onAllNodesWithTag(DELETE_FILE)[1].assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun check_how_many_file_are_displayed_after_removing_the_first() {
        initialize()

        composeRule.onAllNodesWithTag(FILE_ITEM)[0]
            .assertTextEquals("File1")
            .assertIsDisplayed()

        composeRule.onAllNodesWithTag(DELETE_FILE)[0].performClick()

        composeRule.onNodeWithText("File1").assertIsNotDisplayed()
    }

    @Test
    fun removing_all_notes_shows_empty_state() {
        initialize()

        composeRule.onAllNodesWithTag(FILE_ITEM)[0]
            .assertTextEquals("File1")
            .assertIsDisplayed()
        composeRule.onAllNodesWithTag(FILE_ITEM)[1]
            .assertTextEquals("File2")
            .assertIsDisplayed()

        composeRule.onAllNodesWithTag(DELETE_FILE)[0].performClick()
        composeRule.onAllNodesWithTag(DELETE_FILE)[0].performClick()
    }

    //helper method
    private fun initialize(
        isEmptyList: Boolean = false
    ) {
        setup()

        currentFileList.value = if (isEmptyList) {
            emptyList()
        } else {
            createFileList()
        }

        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns 1
        coEvery { getFilesListUseCase.invoke() } returns currentFileList
        coEvery { deleteFileUseCase.invoke(any()) } answers {
            val idToDelete = it.invocation.args[0] as Int
            currentFileList.value = currentFileList.value.filterNot { file -> file.id == idToDelete }
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
            navController.navigateToFile(1)
        }
    }

    private fun createFileList() = listOf(
        FileData(
            name = "File1",
            url = "url1",
            id = 1,
            tripId = 1
        ),
        FileData(
            name = "File2",
            url = "url2",
            id = 2,
            tripId = 1
        )
    )
}