package com.example.wherenow.repository

import com.example.wherenow.database.trip.Trip
import com.example.wherenow.database.trip.TripDatabase
import com.example.wherenow.util.convertLocalDateToTimestampUTC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

interface TripListRepository {
    suspend fun saveDataTile(trip: Trip)
    suspend fun getListDataTile(): List<Trip>
    suspend fun deletedDataTile(id: Int)
    suspend fun getPastTrip(): List<Trip>
    suspend fun getUpcomingTrips(): List<Trip>
    suspend fun getFutureTrip(): List<Trip>
}

class TripListRepositoryImpl(
    private val db: TripDatabase,
    private val dispatchers: Dispatchers
) : TripListRepository {

    private val startDate = convertLocalDateToTimestampUTC(LocalDate.now())
    private val endDate = convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(3))

    override suspend fun saveDataTile(trip: Trip) = db.dao().insertTrip(trip = trip)

    override suspend fun getListDataTile(): List<Trip> = withContext(dispatchers.IO) {
        db.dao().getAllTrips()
    }

    override suspend fun deletedDataTile(id: Int) = db.dao().deleteTrip(id = id)

    override suspend fun getPastTrip(): List<Trip> =
        withContext(dispatchers.IO) {
            db.dao().getPastTrip(startDate)
        }

    override suspend fun getUpcomingTrips(): List<Trip> =
        withContext(dispatchers.IO) {
            db.dao().getTripFromThisMonth(startDate, endDate)
        }

    override suspend fun getFutureTrip(): List<Trip> =
        withContext(dispatchers.IO) {
            db.dao().getFutureTrip(endDate)
        }
}