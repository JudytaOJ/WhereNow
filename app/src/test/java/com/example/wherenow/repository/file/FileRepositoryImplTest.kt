package com.example.wherenow.repository.file

import com.example.wherenow.database.file.File
import com.example.wherenow.database.file.FileDao
import com.example.wherenow.database.file.FileDatabase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FileRepositoryImplTest {

    private val fileDao: FileDao = mockk(relaxed = true)
    private val fileDatabase: FileDatabase = mockk(relaxed = true)

    private lateinit var repository: FileRepository

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveFile delegates to dao`() = runTest {
        //Arrange
        val file = File(
            id = 1,
            name = "file.pdf",
            url = "url",
            tripId = 1
        )
        initialize()
        coEvery { fileDao.saveFile(file) } returns mockk(relaxed = true)
        advanceUntilIdle()
        //Act
        repository.saveFile(file)
        //Assert
        coVerify(exactly = 1) { fileDao.saveFile(file) }
    }

    @Test
    fun `getFilesList returns flow from dao`() = runTest {
        //Arrange
        val fileList = listOf(
            File(
                id = 1,
                name = "file.pdf",
                url = "url",
                tripId = 1
            ),
            File(
                id = 2,
                name = "file2.pdf",
                url = "url2",
                tripId = 2
            )
        )
        val flow = flowOf(fileList)
        coEvery { fileDao.getAllFiles() } returns flow
        initialize()
        //Act
        val result = repository.getFilesList().first()
        //Assert
        assertEquals(fileList, result)
        coVerify { fileDao.getAllFiles() }
    }

    @Test
    fun `deleteFile delegates to dao`() = runTest {
        //Arrange
        coEvery { fileDao.deleteFile(1) } returns mockk(relaxed = true)
        initialize()
        advanceUntilIdle()
        //Act
        repository.deleteFile(1)
        //Assert
        coVerify { fileDao.deleteFile(1) }
    }

    //helper methods
    private fun initialize() {
        coEvery { fileDatabase.dao() } returns fileDao
        repository = FileRepositoryImpl(fileDatabase)
    }
}