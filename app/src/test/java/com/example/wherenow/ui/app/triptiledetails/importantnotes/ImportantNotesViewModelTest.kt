package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.lifecycle.SavedStateHandle
import com.example.wherenow.data.usecases.DeleteImportantNoteUseCase
import com.example.wherenow.data.usecases.GetImportantNotesListUseCase
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesUiIntent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ImportantNotesViewModelTest {
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getImportantNotesListUseCase: GetImportantNotesListUseCase = mockk(relaxed = true)
    private val deleteImportantNoteUseCase: DeleteImportantNoteUseCase = mockk(relaxed = true)

    private lateinit var sut: ImportantNotesViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify load data`() = runTest {
        //Arrange
        initialize()
        //Act
        //Assert
        Assertions.assertEquals(TRIP_ID, sut.uiState.value.tripId)
    }

    @Test
    fun `check display important notes list`() = runTest {
        //Arrange
        coEvery { getImportantNotesListUseCase.invoke() } returns createImportantNotes()
        initialize()
        //Act
        //Assert
        coEvery { getImportantNotesListUseCase.invoke() }
        Assertions.assertEquals(createImportantNotes().filter { trip -> trip.tripId == TRIP_ID }, sut.uiState.value.notesList)
    }

    @Test
    fun `verify navigate to back when onBack clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(ImportantNotesUiIntent.OnBackClicked)
        //Assert
        Assertions.assertEquals(ImportantNotesNavigationEvent.OnBack, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify navigate to add note when onAddNotes clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(ImportantNotesUiIntent.OnAddNotes(TRIP_ID))
        //Assert
        Assertions.assertEquals(ImportantNotesNavigationEvent.OnAddNotes(TRIP_ID), sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify deleted note when onDeleteNote clicked`() = runTest {
        //Arrange
        coEvery { getImportantNotesListUseCase.invoke() } returns createImportantNotes()
        coEvery { deleteImportantNoteUseCase.invoke(TRIP_ID) } returns Unit
        initialize()
        //Act
        sut.onUiIntent(ImportantNotesUiIntent.OnDeleteNote(TRIP_ID))
        advanceUntilIdle()
        //Assert
        coVerify { deleteImportantNoteUseCase.invoke(TRIP_ID) }
        coVerify { getImportantNotesListUseCase.invoke() }
    }

    @Test
    fun `verify navigate to edit note when onEditNote clicked`() = runTest {
        //Arrange
        val item = ImportantNoteItemData(
            title = "Edit title",
            description = "Edit description",
            id = 1,
            tripId = TRIP_ID
        )
        initialize()
        //Act
        sut.onUiIntent(ImportantNotesUiIntent.OnEditNote(item))
        //Assert
        Assertions.assertEquals(ImportantNotesNavigationEvent.OnEditNote(item), sut.navigationEvents.firstOrNull())
    }

    //helper methods
    private fun initialize() {
        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID

        sut = ImportantNotesViewModel(
            savedStateHandle = savedStateHandle,
            getImportantNotesListUseCase = getImportantNotesListUseCase,
            deleteImportantNoteUseCase = deleteImportantNoteUseCase
        )
    }

    //helper constants
    companion object {
        const val TRIP_ID = 1
    }

    private fun createImportantNotes(): List<ImportantNoteItemData> =
        listOf(
            ImportantNoteItemData(
                id = 1,
                title = "Important note 1",
                description = "Don't forget!",
                tripId = TRIP_ID
            ),
            ImportantNoteItemData(
                id = 1,
                title = "Important note 1",
                description = "Don't forget!",
                tripId = TRIP_ID
            )
        )
}