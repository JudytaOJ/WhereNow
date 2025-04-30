package com.example.wherenow.database.file

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [File::class],
    version = 3
)
abstract class FileDatabase : RoomDatabase() {
    abstract fun dao(): FileDao
}