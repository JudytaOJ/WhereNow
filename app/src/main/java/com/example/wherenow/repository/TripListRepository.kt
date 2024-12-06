package com.example.wherenow.repository

import com.example.wherenow.database.Trip
import com.example.wherenow.database.TripDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TripListRepository {
    suspend fun saveDataTile(data: Trip)
    suspend fun getListDataTile(): Flow<List<Trip>>
    suspend fun deletedDataTile(id: Int)
}

class TripListRepositoryImpl @Inject constructor(
    private val db: TripDatabase
) : TripListRepository {

    override suspend fun saveDataTile(data: Trip) = db.dao().insertTrip(data)

    override suspend fun getListDataTile(): Flow<List<Trip>> = db.dao().getAllTrips()

    override suspend fun deletedDataTile(id: Int) = db.dao().deleteTrip(id)
}