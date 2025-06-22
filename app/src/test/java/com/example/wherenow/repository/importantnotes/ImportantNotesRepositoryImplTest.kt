package com.example.wherenow.repository.importantnotes

import com.example.wherenow.database.notes.NoteDatabase
import com.example.wherenow.database.notes.Notes
import com.example.wherenow.database.notes.NotesDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ImportantNotesRepositoryImplTest {

    private val noteDao: NotesDao = mockk(relaxed = true)
    private val noteDatabase: NoteDatabase = mockk(relaxed = true)
    private val dispatchers: Dispatchers = mockk(relaxed = true)

    private lateinit var repository: ImportantNotesRepository

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveNote delegates to dao`() = runTest {
        //Arrange
        val note = createNote()
        coEvery { noteDao.saveNote(note) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveNote(note)
        //Assert
        coVerify(exactly = 1) { noteDao.saveNote(note) }
    }

    @Test
    fun `getNotesList returns list from dao using dispatcher`() = runTest {
        //Arrange
        val notesList = listOf(
            Notes(
                id = 1,
                title = "title",
                description = "description",
                tripId = 1
            ),
            Notes(
                id = 2,
                title = "title2",
                description = "description2",
                tripId = 2
            )
        )
        coEvery { noteDao.getAllNotes() } returns notesList
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getNotesList()
        //Assert
        assertEquals(notesList, result)
        coVerify { noteDao.getAllNotes() }
    }

    @Test
    fun `getNotesList returns empty list when dao returns nothing`() = runTest {
        //Arrange
        coEvery { noteDao.getAllNotes() } returns emptyList()
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getNotesList()
        //Assert
        Assertions.assertTrue(result.isEmpty())
        coVerify { noteDao.getAllNotes() }
    }

    @Test
    fun `deleteNote delegates to dao`() = runTest {
        //Arrange
        coEvery { noteDao.deleteNote(1) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        repository.deleteNote(1)
        //Assert
        coVerify { noteDao.deleteNote(1) }
    }

    @Test
    fun `updateNote delegates to dao`() = runTest {
        //Arrange
        val note = createNote()
        initialize()
        advanceUntilIdle()
        coEvery { noteDao.updateNote(note) } returns mockk(relaxed = true)
        //Act
        repository.updateNote(note)
        //Assert
        coVerify { noteDao.updateNote(note) }
    }

    @Test
    fun `saveNote followed by getNotesList calls dao in correct order`() = runTest {
        //Arrange
        val note = createNote()
        coEvery { noteDao.saveNote(note) } returns mockk(relaxed = true)
        coEvery { noteDao.getAllNotes() } returns listOf(note)
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveNote(note)
        repository.getNotesList()
        //Assert
        coVerifySequence {
            noteDao.saveNote(note)
            noteDao.getAllNotes()
        }
    }

    //hel[er methods
    private fun initialize() {
        coEvery { noteDatabase.dao() } returns noteDao
        repository = ImportantNotesRepositoryImpl(
            db = noteDatabase,
            dispatchers = dispatchers
        )
    }

    private fun createNote(): Notes = Notes(
        id = 1,
        title = "title",
        description = "description",
        tripId = 1
    )
}