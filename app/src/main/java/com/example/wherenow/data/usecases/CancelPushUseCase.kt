package com.example.wherenow.data.usecases

import com.example.wherenow.notification.NotificationScheduler

class CancelPushUseCase internal constructor(
    private val notificationScheduler: NotificationScheduler
) {
    operator fun invoke(id: Int) {
        notificationScheduler.cancelReminder(id.toString())
    }
}