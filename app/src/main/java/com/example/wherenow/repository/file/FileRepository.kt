package com.example.wherenow.repository.file

import com.example.wherenow.database.file.File
import com.example.wherenow.database.file.FileDatabase
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    suspend fun saveFile(file: File)
    fun getFilesList(): Flow<List<File>>
    suspend fun deleteFile(id: Int)
}

class FileRepositoryImpl(
    private val db: FileDatabase
) : FileRepository {

    override suspend fun saveFile(file: File) = db.dao().saveFile(file = file)

    override fun getFilesList(): Flow<List<File>> = db.dao().getAllFiles()

    override suspend fun deleteFile(id: Int) = db.dao().deleteFile(id = id)
}