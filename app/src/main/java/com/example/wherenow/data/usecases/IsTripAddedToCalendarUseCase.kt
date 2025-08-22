package com.example.wherenow.data.usecases

import com.example.wherenow.repository.calendar.CalendarFlightRepository

class IsTripAddedToCalendarUseCase(
    private val repository: CalendarFlightRepository
) {
    suspend operator fun invoke(tripId: Int): Boolean {
        return repository.isTripAddedToCalendar(tripId)
    }
}