package com.example.wherenow.database.file

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [File::class],
    version = 4
)
abstract class FileDatabase : RoomDatabase() {
    abstract fun dao(): FileDao
}