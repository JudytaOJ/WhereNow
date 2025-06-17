package com.example.wherenow.notification

class NotificationScheduler(private val reminderService: TravelReminderService) {
    fun scheduleReminder(tripId: String, tripDate: Long) {
        reminderService.scheduleReminder(tripId, tripDate)
    }

    fun cancelReminder(tripId: String) {
        reminderService.cancelReminder(tripId)
    }
}