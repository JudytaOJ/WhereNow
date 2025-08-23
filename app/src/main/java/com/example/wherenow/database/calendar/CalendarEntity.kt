package com.example.wherenow.database.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar")
data class CalendarEntity(
    @ColumnInfo("tripId")
    @PrimaryKey val tripId: Int,

    @ColumnInfo("addedToCalendar")
    val addedToCalendar: Boolean
)