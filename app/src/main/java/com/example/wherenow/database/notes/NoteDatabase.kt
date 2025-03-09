package com.example.wherenow.database.notes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Notes::class],
    version = 4
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun dao(): NotesDao
}