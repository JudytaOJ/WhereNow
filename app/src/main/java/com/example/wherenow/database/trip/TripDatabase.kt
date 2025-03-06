package com.example.wherenow.database.trip

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Trip::class],
    version = 12
)
abstract class TripDatabase : RoomDatabase() {
    abstract fun dao(): TripDao
}