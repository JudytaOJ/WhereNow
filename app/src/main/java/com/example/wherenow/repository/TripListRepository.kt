package com.example.wherenow.repository

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.database.trip.TripDatabase
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

interface TripListRepository {
    suspend fun saveDataTile(trip: Trip)
    suspend fun getListDataTile(): Flow<List<Trip>>
    suspend fun deletedDataTile(id: Int)
    suspend fun getPastTrip(): Flow<List<Trip>>
    suspend fun getUpcomingTrips(): Flow<List<Trip>>
    suspend fun getFutureTrip(): Flow<List<Trip>>
}

class TripListRepositoryImpl @Inject constructor(
    private val db: TripDatabase
) : TripListRepository {

    private val startDate = convertLocalDateToTimestampUTC(LocalDate.now())
    private val endDate = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(3))

    override suspend fun saveDataTile(trip: Trip) = db.dao().insertTrip(trip = trip)

    override suspend fun getListDataTile(): Flow<List<Trip>> = db.dao().getAllTrips()

    override suspend fun deletedDataTile(id: Int) = db.dao().deleteTrip(id = id)

    override suspend fun getPastTrip(): Flow<List<Trip>> = db.dao().getPastTrip(startDate)

    override suspend fun getUpcomingTrips(): Flow<List<Trip>> = db.dao().getTripFromThisMonth(startDate, endDate)

    override suspend fun getFutureTrip(): Flow<List<Trip>> = db.dao().getFutureTrip(endDate)
}