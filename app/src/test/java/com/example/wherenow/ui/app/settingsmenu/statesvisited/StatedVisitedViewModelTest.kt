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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatedVisitedViewModelTest {
    private val saveStatesVisitedUseCase: SaveStatesVisitedUseCase = mockk(relaxed = true)
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)

    private lateinit var sut: StatedVisitedViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
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
        Assertions.assertEquals(StatedVisitedNavigationEvent.OnBackNavigation, sut.navigationEvents.firstOrNull())
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
        Assertions.assertEquals(listStateItem(), sut.uiState.value.statesList)
        //first record in list
        Assertions.assertEquals(ALABAMA, sut.uiState.value.statesList[0].text)
        Assertions.assertEquals(1, sut.uiState.value.statesList[0].id)
        Assertions.assertEquals(0, sut.uiState.value.statesList[0].imageRes)
        Assertions.assertEquals(false, sut.uiState.value.statesList[0].isChecked)
        //second record in list
        Assertions.assertEquals(CALIFORNIA, sut.uiState.value.statesList[1].text)
        Assertions.assertEquals(2, sut.uiState.value.statesList[1].id)
        Assertions.assertEquals(1, sut.uiState.value.statesList[1].imageRes)
        Assertions.assertEquals(false, sut.uiState.value.statesList[1].isChecked)
        //third record in list
        Assertions.assertEquals(MONTANA, sut.uiState.value.statesList[2].text)
        Assertions.assertEquals(3, sut.uiState.value.statesList[2].id)
        Assertions.assertEquals(2, sut.uiState.value.statesList[2].imageRes)
        Assertions.assertEquals(true, sut.uiState.value.statesList[2].isChecked)
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
        Assertions.assertEquals(updatedList, sut.uiState.value.statesList)
        Assertions.assertEquals(true, sut.uiState.value.statesList[0].isChecked)
        Assertions.assertEquals(true, sut.uiState.value.statesList[1].isChecked)
        Assertions.assertEquals(true, sut.uiState.value.statesList[2].isChecked)
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