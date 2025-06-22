package com.example.wherenow.repository

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.database.trip.TripDatabase
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

interface TripListRepository {
    suspend fun saveDataTile(trip: Trip): Int
    suspend fun getListDataTile(): List<Trip>
    suspend fun deletedDataTile(id: Int)
    suspend fun getPastTrip(): List<Trip>
    suspend fun getUpcomingTrips(): List<Trip>
    suspend fun getFutureTrip(): List<Trip>
}

open class TripListRepositoryImpl(
    private val db: TripDatabase,
) : TripListRepository {

    protected open val startDate = convertLocalDateToTimestampUTC(LocalDate.now())
    protected open val endDate = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(3))

    override suspend fun saveDataTile(trip: Trip): Int = db.dao().insertTrip(trip).toInt()

    override suspend fun getListDataTile(): List<Trip> = withContext(Dispatchers.IO) {
        db.dao().getAllTrips()
    }

    override suspend fun deletedDataTile(id: Int) = db.dao().deleteTrip(id = id)

    override suspend fun getPastTrip(): List<Trip> =
        withContext(Dispatchers.IO) {
            db.dao().getPastTrip(startDate)
        }

    override suspend fun getUpcomingTrips(): List<Trip> =
        withContext(Dispatchers.IO) {
            db.dao().getTripFromThisMonth(startDate, endDate)
        }

    override suspend fun getFutureTrip(): List<Trip> =
        withContext(Dispatchers.IO) {
            db.dao().getFutureTrip(endDate)
        }
}