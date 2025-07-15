package com.example.wherenow.ui.app.settingsmenu.appTheme

import com.example.wherenow.data.usecases.GetThemeModeUseCase
import com.example.wherenow.data.usecases.SetThemeModeUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppThemeViewModelTest {
    private val getThemeModeUseCase: GetThemeModeUseCase = mockk(relaxed = true)
    private val setThemeModeUseCase: SetThemeModeUseCase = mockk(relaxed = true)

    private lateinit var sut: AppThemeViewModel

    @Before
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init emits theme mode from use case`() = runTest {
        //Arrange
        val flow = MutableStateFlow(false)
        coEvery { getThemeModeUseCase() } returns flow
        initialize()
        advanceUntilIdle()
        //Act
        //Assert
        coVerify { getThemeModeUseCase() }
        assertEquals(false, sut.isDarkTheme.value)

        flow.value = true
        assertEquals(true, sut.isDarkTheme.value)
    }

    @Test
    fun `toggleTheme calls setThemeModeUseCase with toggled value`() = runTest {
        //Arrange
        val flow = MutableStateFlow(false)
        coEvery { getThemeModeUseCase() } returns flow
        coEvery { setThemeModeUseCase(any()) } just Runs
        initialize()
        advanceUntilIdle()
        //Act
        sut.toggleTheme()
        advanceUntilIdle()
        //Assert
        coVerify { getThemeModeUseCase() }
        coVerify { setThemeModeUseCase(true) }

        flow.value = true
        sut.toggleTheme()
        advanceUntilIdle()
        coVerify { setThemeModeUseCase(false) }
    }

    //helper methods
    private fun initialize() {
        sut = AppThemeViewModel(
            getThemeModeUseCase = getThemeModeUseCase,
            setThemeModeUseCase = setThemeModeUseCase
        )
    }
}