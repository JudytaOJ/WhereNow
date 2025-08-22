package com.example.wherenow.database.calendar

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CalendarEntity::class],
    version = 6
)

abstract class CalendarDatabase : RoomDatabase() {
    abstract fun dao(): CalendarDao
}