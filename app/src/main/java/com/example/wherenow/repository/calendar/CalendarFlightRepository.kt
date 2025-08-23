package com.example.wherenow.repository.calendar

import android.content.ContentResolver
import android.content.ContentValues
import android.icu.util.TimeZone
import android.provider.CalendarContract
import android.util.Log
import com.example.wherenow.database.calendar.CalendarDatabase
import com.example.wherenow.database.calendar.CalendarEntity
import com.example.wherenow.repository.calendar.models.CalendarFlight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CalendarFlightRepository {
    fun getCalendarId(): Long?
    fun addEvent(event: CalendarFlight, calendarId: Long): Boolean
    suspend fun saveTripAddedToCalendar(tripId: Int, added: Boolean)
    fun observeTripAddedToCalendar(tripId: Int): Flow<Boolean>
    suspend fun isTripAddedToCalendar(tripId: Int): Boolean
    fun verifyEventExist(startTimeMillis: Long, title: String): Boolean
}

class CalendarFlightRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val calendarDb: CalendarDatabase
) : CalendarFlightRepository {

    override fun getCalendarId(): Long? {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )
        val uri = CalendarContract.Calendars.CONTENT_URI

        val cursor = contentResolver.query(
            /* uri = */ uri,
            /* projection = */ projection,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(0)
                val name = it.getString(1)
                Log.d("CalendarRepo", "Found calendar: $name ($id)")
                return id
            }
        }
        return null
    }

    override fun addEvent(event: CalendarFlight, calendarId: Long): Boolean {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, event.startTimeMillis)
            put(CalendarContract.Events.DTEND, event.endTimeMillis)
            put(CalendarContract.Events.TITLE, event.title)
            put(CalendarContract.Events.DESCRIPTION, event.description)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        Log.d("CalendarRepo", "Event added: $uri")
        return uri != null
    }

    override suspend fun saveTripAddedToCalendar(tripId: Int, added: Boolean) {
        calendarDb.dao().insertFlight(CalendarEntity(tripId, addedToCalendar = added))
    }

    override fun observeTripAddedToCalendar(tripId: Int): Flow<Boolean> {
        return calendarDb.dao().observeFlight(tripId).map { it?.addedToCalendar ?: false }
    }

    override suspend fun isTripAddedToCalendar(tripId: Int): Boolean {
        return calendarDb.dao().isTripAdded(tripId)
    }

    override fun verifyEventExist(startTimeMillis: Long, title: String): Boolean {
        val projection = arrayOf(CalendarContract.Events._ID)
        val selection = "${CalendarContract.Events.DTSTART} = ? AND ${CalendarContract.Events.TITLE} = ?"
        val selectionArgs = arrayOf(startTimeMillis.toString(), title)

        val cursor = contentResolver.query(
            /* uri = */ CalendarContract.Events.CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ selection,
            /* selectionArgs = */ selectionArgs,
            /* sortOrder = */ null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return true // event exists
            }
        }
        return false
    }
}