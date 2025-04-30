package com.example.wherenow.repository.file

import com.example.wherenow.database.file.File
import com.example.wherenow.database.file.FileDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface FileRepository {
    suspend fun saveFile(file: File)
    suspend fun getFilesList(): List<File>
    suspend fun deleteFile(id: Int)
}

class FileRepositoryImpl(
    private val db: FileDatabase,
    private val dispatchers: Dispatchers
) : FileRepository {

    override suspend fun saveFile(file: File) = db.dao().saveFile(file = file)

    override suspend fun getFilesList(): List<File> =
        withContext(dispatchers.IO) {
            db.dao().getAllFiles()
        }

    override suspend fun deleteFile(id: Int) = db.dao().deleteFile(id = id)
}