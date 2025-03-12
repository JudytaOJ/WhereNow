package com.example.wherenow.repository

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.database.trip.TripDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TripListRepository {
    suspend fun saveDataTile(trip: Trip)
    suspend fun getListDataTile(): Flow<List<Trip>>
    suspend fun deletedDataTile(id: Int)
    suspend fun getPastTrip(): Flow<List<Trip>>
    suspend fun getTripFromThisMonth(): Flow<List<Trip>>
    suspend fun getFutureTrip(): Flow<List<Trip>>
}

class TripListRepositoryImpl @Inject constructor(
    private val db: TripDatabase
) : TripListRepository {

    override suspend fun saveDataTile(trip: Trip) = db.dao().insertTrip(trip = trip)

    override suspend fun getListDataTile(): Flow<List<Trip>> = db.dao().getAllTrips()

    override suspend fun deletedDataTile(id: Int) = db.dao().deleteTrip(id = id)

    override suspend fun getPastTrip(): Flow<List<Trip>> = db.dao().getPastTrip()

    override suspend fun getTripFromThisMonth(): Flow<List<Trip>> = db.dao().getTripFromThisMonth()

    override suspend fun getFutureTrip(): Flow<List<Trip>> = db.dao().getFutureTrip()
}