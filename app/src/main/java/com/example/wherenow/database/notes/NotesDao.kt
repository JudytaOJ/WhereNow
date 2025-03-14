package com.example.wherenow.database.notes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(notes: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Notes>

    @Query("DELETE FROM notes where id = :id")
    suspend fun deleteNote(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Notes)
}