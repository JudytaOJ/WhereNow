package com.example.wherenow.database.trip

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Trip::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TripDatabase : RoomDatabase() {
    abstract fun dao(): TripDao
}