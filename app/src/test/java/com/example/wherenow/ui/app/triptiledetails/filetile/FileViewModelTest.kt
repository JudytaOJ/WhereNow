package com.example.wherenow.ui.app.triptiledetails.filetile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.example.wherenow.data.usecases.DeleteFileUseCase
import com.example.wherenow.data.usecases.GetFileNameUseCase
import com.example.wherenow.data.usecases.GetFilesListUseCase
import com.example.wherenow.data.usecases.SaveFileUseCase
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileUiIntent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FileViewModelTest {
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val getFilesListUseCase: GetFilesListUseCase = mockk(relaxed = true)
    private val saveFileUseCase: SaveFileUseCase = mockk(relaxed = true)
    private val deleteFileUseCase: DeleteFileUseCase = mockk(relaxed = true)
    private val getFileNameUseCase: GetFileNameUseCase = mockk(relaxed = true)

    private lateinit var sut: FileViewModel

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify navigate to back when onBack clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(FileUiIntent.OnBackClicked)
        //Assert
        Assertions.assertEquals(FileNavigationEvent.OnBackClicked, sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify navigate to open file when openFile clicked`() = runTest {
        //Arrange
        initialize()
        //Act
        sut.onUiIntent(FileUiIntent.OpenFile(getFileData()))
        //Assert
        Assertions.assertEquals(FileNavigationEvent.OnOpenFile(getFileData()), sut.navigationEvents.firstOrNull())
    }

    @Test
    fun `verify save file to repository`() = runTest {
        //Arrange
        val newFileData = FileData(
            name = FILE_2,
            url = URL_1,
            id = 2,
            tripId = TRIP_ID_1
        )
        coEvery { saveFileUseCase.invoke(any()) } returns mockk(relaxed = true)
        initialize()
        //Act
        sut.onUiIntent(FileUiIntent.AddFile(newFileData))
        //Assert
        coVerify { saveFileUseCase.invoke(any()) }
        Assertions.assertEquals(TRIP_ID_1, sut.uiState.value.tripId)
    }

    @Test
    fun `verify delete file to repository`() = runTest {
        //Arrange
        coEvery { deleteFileUseCase.invoke(getFileData().id) } returns mockk(relaxed = true)
        initialize()
        //Act
        sut.onUiIntent(FileUiIntent.OnDeleteFile(getFileData().id))
        //Assert
        coVerify { deleteFileUseCase.invoke(getFileData().id) }
        assertEquals(emptyList(), sut.uiState.value.fileList)
    }

    @Test
    fun `observeFiles filters by tripId and sorts by name then updates uiState`() = runTest {
        //Arrange
        val fileDataList = getFilesDataList()
        val flow = flowOf(fileDataList)
        coEvery { getFilesListUseCase.invoke() } returns flow
        initialize()
        //Act
        //Assert
        coVerify { getFilesListUseCase.invoke() }
        val expectedList = listOf(
            FileData(
                name = FILE_2,
                url = URL_1,
                id = 2,
                tripId = TRIP_ID_1
            ),
            FileData(
                name = FILE_3,
                url = URL_1,
                id = 3,
                tripId = TRIP_ID_1
            )
        )
        val actual = sut.uiState.value.fileList
        assertEquals(expectedList, actual)
    }

    @Test
    fun `getFileNameFromUri returns correct file name from use case`() = runTest {
        //Arrange
        val uri = mockk<Uri>()
        val expectedFileName = "ticket.pdf"
        initialize()
        coEvery { getFileNameUseCase.invoke(uri) } returns expectedFileName
        //Act
        //Assert
        assertEquals(expectedFileName, sut.getFileNameFromUri(uri))
        coVerify { getFileNameUseCase.invoke(uri) }
    }

    //helper methods
    private fun initialize() {
        every { savedStateHandle.get<Int>(TripTileDetailsTag.TRIP_ID) } returns TRIP_ID_1

        sut = FileViewModel(
            savedStateHandle = savedStateHandle,
            getFilesListUseCase = getFilesListUseCase,
            saveFileUseCase = saveFileUseCase,
            deleteFileUseCase = deleteFileUseCase,
            getFileNameUseCase = getFileNameUseCase
        )
    }

    private fun getFileData(): FileData = FileData(
        name = FILE_1,
        url = URL_1,
        id = 1,
        tripId = TRIP_ID_1
    )

    private fun getFilesDataList(): List<FileData> =
        listOf(
            FileData(
                name = FILE_2,
                url = URL_1,
                id = 2,
                tripId = TRIP_ID_1
            ),
            FileData(
                name = FILE_3,
                url = URL_1,
                id = 3,
                tripId = TRIP_ID_1
            ),
            FileData(
                name = FILE_4,
                url = URL_1,
                id = 4,
                tripId = TRIP_ID_2
            )
        )

    //helper constants
    companion object {
        const val TRIP_ID_1 = 1
        const val TRIP_ID_2 = 2
        const val FILE_1 = "File1"
        const val FILE_2 = "File2"
        const val FILE_3 = "File3"
        const val FILE_4 = "File4"
        const val URL_1 = "content://com.example.provider/files/sample.pdf"
    }
}