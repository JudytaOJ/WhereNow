package com.example.wherenow.data.usecases

import com.example.wherenow.repository.calendar.CalendarFlightRepository

class SaveTripAddedToCalendarUseCase(
    private val repository: CalendarFlightRepository
) {
    suspend operator fun invoke(tripId: Int, added: Boolean) {
        repository.saveTripAddedToCalendar(tripId, added)
    }
}