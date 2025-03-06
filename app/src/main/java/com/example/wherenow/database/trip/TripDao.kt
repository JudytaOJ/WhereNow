package com.example.wherenow.database.trip

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip)

    @Query("SELECT * FROM trip_details")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("DELETE FROM trip_details where id = :id")
    suspend fun deleteTrip(id: Int)
}