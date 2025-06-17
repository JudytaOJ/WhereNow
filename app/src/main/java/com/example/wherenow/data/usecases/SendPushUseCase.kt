package com.example.wherenow.data.usecases

import com.example.wherenow.notification.NotificationScheduler

class SendPushUseCase internal constructor(
    private val notificationScheduler: NotificationScheduler
) {
    operator fun invoke(id: Int, date: Long) {
        notificationScheduler.scheduleReminder(
            tripId = id.toString(),
            tripDate = date
        )
    }
}