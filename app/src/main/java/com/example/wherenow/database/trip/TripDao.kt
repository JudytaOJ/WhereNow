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

    @Query("SELECT * FROM trip_details WHERE date < :endDate")
    fun getPastTrip(endDate: Long): Flow<List<Trip>>

    @Query("SELECT * FROM trip_details WHERE date >= :startDate AND date <= :endDate")
    fun getTripFromThisMonth(startDate: Long, endDate: Long): Flow<List<Trip>>

    @Query("SELECT * FROM trip_details WHERE date > :startDate")
    fun getFutureTrip(startDate: Long): Flow<List<Trip>>
}