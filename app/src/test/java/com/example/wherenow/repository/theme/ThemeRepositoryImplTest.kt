package com.example.wherenow.repository.theme

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeRepositoryImplTest {

    private val dataStore: ThemeDataStore = mockk(relaxed = true)

    private lateinit var repository: ThemeRepository

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setDarkMode delegates to dataStore`() = runTest {
        //Arrange
        coEvery { dataStore.setDarkMode(true) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        repository.setDarkMode(true)
        //Assert
        coVerify(exactly = 1) { dataStore.setDarkMode(true) }
    }

    @Test
    fun `isDarkMode emits values from dataStore`() = runTest {
        //Arrange
        val flow = flowOf(true, false)
        coEvery { dataStore.themeFlow } returns flow
        initialize()
        advanceUntilIdle()
        //Act
        val results = repository.isDarkMode.toList()
        //Assert
        assertEquals(listOf(true, false), results)
        coVerify { dataStore.themeFlow }
    }

    private fun initialize() {
        repository = ThemeRepositoryImpl(dataStore)
    }
}