package com.example.wherenow.database.file

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFile(file: File)

    @Query("SELECT * FROM file")
    fun getAllFiles(): Flow<List<File>>

    @Query("DELETE FROM file where id = :id")
    suspend fun deleteFile(id: Int)
}