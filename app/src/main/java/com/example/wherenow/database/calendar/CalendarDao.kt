package com.example.wherenow.database.calendar

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlight(calendar: CalendarEntity)

    @Query("SELECT * FROM calendar WHERE tripId = :tripId")
    fun observeFlight(tripId: Int): Flow<CalendarEntity?>

    @Query("SELECT addedToCalendar FROM calendar WHERE tripId = :tripId")
    suspend fun isTripAdded(tripId: Int): Boolean
}