package com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote

import androidx.lifecycle.SavedStateHandle
import com.example.wherenow.data.usecases.SaveImportantNoteUseCase
import com.example.wherenow.data.usecases.UpdateImportantNoteUseCase
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteUiIntent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BlankNoteViewModelTest {
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val saveImportantNoteUseCase: SaveImportantNoteUseCase = mockk(relaxed = true)
    private val updateImportantNoteUseCase: UpdateImportantNoteUseCase = mockk(relaxed = true)

    private lateinit var sut: BlankNoteViewModel

    @Before
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify load data on init`() = runTest {
        //Arrange
        val titleNote = "Title note"
        val description = "Description note"
        initialize()
        //Act
        //Assert
        assertEquals(titleNote, sut.uiState.value.titleNote)
        assertEquals(description, sut.uiState.value.descriptionNote)
        assertEquals(ID_NOTE, sut.uiState.value.id)
        assertEquals(TRIP_ID, sut.uiState.value.tripId)
    }

    @Test
    fun `verify navigate to back when onBack clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(BlankNoteUiIntent.OnBackClicked)
        //Assert
        assertEquals(BlankNoteNavigationEvent.OnBackClicked, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify update title when OnUpdateTitleNote clicked`() = runTest {
        //Arrange
        val newValue = "Change title - new title"
        initialize()
        //Act
        sut.onUiIntent(BlankNoteUiIntent.OnUpdateTitleNote(newValue))
        //Assert
        assertEquals(newValue, sut.uiState.value.titleNote)
    }

    @Test
    fun `verify update description when OnUpdateDescriptionNote clicked`() = runTest {
        //Arrange
        val newValue = "Change description - new description"
        initialize()
        //Act
        sut.onUiIntent(BlankNoteUiIntent.OnUpdateDescriptionNote(newValue))
        //Assert
        assertEquals(newValue, sut.uiState.value.descriptionNote)
    }

    @Test
    fun `verify flow add or edit note - depends on id (condition is true)`() = runTest {
        //Arrange
        val note = ImportantNoteItemData(
            id = 2,
            title = TITLE,
            description = DESCRIPTION,
            tripId = TRIP_ID
        )
        coEvery { updateImportantNoteUseCase.invoke(note) } returns Unit
        coEvery { saveImportantNoteUseCase.invoke(note) } returns Unit
        initialize()
        //Act
        sut.onUiIntent(BlankNoteUiIntent.NextClickedAddOrEditNote(note))
        //Assert
        if (note.id == ID_NOTE) {
            coVerify { updateImportantNoteUseCase.invoke(note) }
            assertEquals(note.title, sut.uiState.value.titleNote)
            assertEquals(note.description, sut.uiState.value.descriptionNote)
        }
        coVerify { saveImportantNoteUseCase.invoke(note) }
        assertEquals(BlankNoteNavigationEvent.AddClickedEvent(TRIP_ID), sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify flow add or edit note - depends on id (condition is false)`() = runTest {
        //Arrange
        val note = ImportantNoteItemData(
            id = 1,
            title = TITLE,
            description = DESCRIPTION,
            tripId = TRIP_ID
        )
        coEvery { saveImportantNoteUseCase.invoke(note) } returns Unit
        initialize()
        //Act
        sut.onUiIntent(BlankNoteUiIntent.NextClickedAddOrEditNote(note))
        //Assert
        coVerify { saveImportantNoteUseCase.invoke(note) }
        assertEquals(BlankNoteNavigationEvent.AddClickedEvent(TRIP_ID), sut.navigationEvents.firstOrNull())
    }

    //helper methods
    private fun initialize() {
        every { savedStateHandle.get<String>(TripTileDetailsTag.TITLE_EDIT_NOTE) } returns TITLE
        every { savedStateHandle.get<String>(TripTileDetailsTag.DESCRIPTION_EDIT_NOTE) } returns DESCRIPTION
        every { savedStateHandle.get<Int>(TripTileDetailsTag.ID_NOTE) } returns ID_NOTE
        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID

        sut = BlankNoteViewModel(
            savedStateHandle = savedStateHandle,
            saveImportantNoteUseCase = saveImportantNoteUseCase,
            updateImportantNoteUseCase = updateImportantNoteUseCase
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    //helper constants
    companion object {
        const val TRIP_ID = 2
        const val ID_NOTE = 2
        const val TITLE = "Title note"
        const val DESCRIPTION = "Description note"
    }
}