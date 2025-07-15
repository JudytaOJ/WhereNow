package com.example.wherenow.repository.statesvisited

import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
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
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class StatesVisitedRepositoryImplTest {

    private val dataStore: StatesVisitedDataStore = mockk(relaxed = true)

    private lateinit var repository: StatesVisitedRepository

    @Before
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveStatesVisitedList delegates to dataStore for each item`() = runTest {
        //Arrange
        val states = listOf(
            StateItem(id = 1, text = "Alabama", isChecked = true, imageRes = 1),
            StateItem(id = 2, text = "Alaska", isChecked = false, imageRes = 2)
        )
        coEvery { dataStore.saveStateVisited(any(), any()) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveStatesVisitedList(states)
        //Assert
        coVerifySequence {
            dataStore.saveStateVisited(1, true)
            dataStore.saveStateVisited(2, false)
        }
    }

    @Test
    fun `saveStatesVisitedList does nothing for empty list`() = runTest {
        //Arrange
        initialize()
        advanceUntilIdle()
        //Act
        repository.saveStatesVisitedList(emptyList())
        //Assert
        coVerify(exactly = 0) { dataStore.saveStateVisited(any(), any()) }
    }

    @Test
    fun `getStatesVisitedList returns result from dataStore`() = runTest {
        //Arrange
        val inputList = listOf(StateItem(id = 1, text = "Alabama", isChecked = false, imageRes = 1))
        val expectedResult =
            listOf(StateItem(id = 1, text = "Alabama", isChecked = true, imageRes = 1))
        coEvery { dataStore.getAllVisitedStates(inputList) } returns expectedResult
        initialize()
        advanceUntilIdle()
        //Act
        val result = repository.getStatesVisitedList(inputList)
        //Assert
        assertEquals(expectedResult, result)
        coVerify { dataStore.getAllVisitedStates(inputList) }
    }

    @Test
    fun `getStatesVisitedList propagates exception from dataStore`() = runTest {
        //Arrange
        val inputList = listOf(StateItem(id = 1, text = "Alabama", isChecked = true, imageRes = 1))
        coEvery { dataStore.getAllVisitedStates(inputList) } throws RuntimeException("Error")
        initialize()
        advanceUntilIdle()
        //Act & Assert
        try {
            repository.getStatesVisitedList(inputList)
            fail("Expected RuntimeException was not thrown")
        } catch (e: RuntimeException) {
            assertEquals("Error", e.message)
        }
    }

    //helper methods
    private fun initialize() {
        repository = StatesVisitedRepositoryImpl(dataStore)
    }
}