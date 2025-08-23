package com.example.wherenow.data.usecases

import com.example.wherenow.repository.calendar.CalendarFlightRepository

class SyncCalendarEventsUseCase(
    private val repository: CalendarFlightRepository
) {
    suspend operator fun invoke(tripId: Int, startTimeMillis: Long, title: String) {
        val exists = repository.verifyEventExist(startTimeMillis, title)
        repository.saveTripAddedToCalendar(tripId = tripId, added = exists)
    }
}