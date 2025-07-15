package com.example.wherenow.ui.app.settingsmenu.statesvisited

import android.content.Context
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.data.usecases.SaveStatesVisitedUseCase
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesVisitedUiIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class StatedVisitedViewModelTest {
    private val saveStatesVisitedUseCase: SaveStatesVisitedUseCase = mockk(relaxed = true)
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)

    private lateinit var sut: StatedVisitedViewModel

    @Before
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify navigate to back when click back arrow`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(StatesVisitedUiIntent.OnBackClicked)
        //Assert
        assertEquals(StatedVisitedNavigationEvent.OnBackNavigation, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify load date`() = runTest {
        //Arrange
        initialize()
        advanceUntilIdle()
        //Act
        sut.loadData(context)
        //Assert
        coVerify { getStatesVisitedUseCase.invoke(any()) }
        assertEquals(listStateItem(), sut.uiState.value.statesList)
        //first record in list
        assertEquals(ALABAMA, sut.uiState.value.statesList[0].text)
        assertEquals(1, sut.uiState.value.statesList[0].id)
        assertEquals(0, sut.uiState.value.statesList[0].imageRes)
        assertEquals(false, sut.uiState.value.statesList[0].isChecked)
        //second record in list
        assertEquals(CALIFORNIA, sut.uiState.value.statesList[1].text)
        assertEquals(2, sut.uiState.value.statesList[1].id)
        assertEquals(1, sut.uiState.value.statesList[1].imageRes)
        assertEquals(false, sut.uiState.value.statesList[1].isChecked)
        //third record in list
        assertEquals(MONTANA, sut.uiState.value.statesList[2].text)
        assertEquals(3, sut.uiState.value.statesList[2].id)
        assertEquals(2, sut.uiState.value.statesList[2].imageRes)
        assertEquals(true, sut.uiState.value.statesList[2].isChecked)
    }

    @Test
    fun `verify onCheckboxToggled method and updated list`() = runTest {
        //Arrange
        val updatedList = listOf(
            StateItem(text = ALABAMA, imageRes = 0, id = 1, isChecked = true),
            StateItem(text = CALIFORNIA, imageRes = 1, id = 2, isChecked = true),
            StateItem(text = MONTANA, imageRes = 2, id = 3, isChecked = true)
        )
        coEvery { saveStatesVisitedUseCase.invoke(updatedList) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        sut.loadData(context)
        sut.onUiIntent(StatesVisitedUiIntent.OnCheckboxToggled(id = 1, isChecked = true))
        sut.onUiIntent(StatesVisitedUiIntent.OnCheckboxToggled(id = 2, isChecked = true))
        sut.onUiIntent(StatesVisitedUiIntent.OnCheckboxToggled(id = 3, isChecked = true))
        advanceUntilIdle()
        //Assert
        coVerify { getStatesVisitedUseCase.invoke(any()) }
        coVerify { saveStatesVisitedUseCase.invoke(updatedList) }
        assertEquals(updatedList, sut.uiState.value.statesList)
        assertEquals(true, sut.uiState.value.statesList[0].isChecked)
        assertEquals(true, sut.uiState.value.statesList[1].isChecked)
        assertEquals(true, sut.uiState.value.statesList[2].isChecked)
    }

    @Test
    fun `verify mark animation shown action`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(StatesVisitedUiIntent.MarkAnimationShown)
        //Assert
        assertEquals(true, sut.uiState.value.hasShownAnimation)
        assertEquals(false, sut.uiState.value.showAnimation)
    }

    @Test
    fun `verify reset animation shown action`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(StatesVisitedUiIntent.ResetAnimation)
        //Assert
        assertEquals(false, sut.uiState.value.hasShownAnimation)
        assertEquals(true, sut.uiState.value.showAnimation)
    }

    //helper constants
    companion object {
        const val ALABAMA = "Alabama"
        const val CALIFORNIA = "California"
        const val MONTANA = "Montana"
    }

    //helper methods
    private fun initialize() {
        coEvery { getStatesVisitedUseCase.invoke(any()) } returns listStateItem()

        sut = StatedVisitedViewModel(
            saveStatesVisitedUseCase = saveStatesVisitedUseCase,
            getStatesVisitedUseCase = getStatesVisitedUseCase
        )
    }

    private fun listStateItem(): List<StateItem> {
        return listOf(
            StateItem(text = ALABAMA, imageRes = 0, id = 1, isChecked = false),
            StateItem(text = CALIFORNIA, imageRes = 1, id = 2, isChecked = false),
            StateItem(text = MONTANA, imageRes = 2, id = 3, isChecked = true)
        )
    }
}