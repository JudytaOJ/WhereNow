package com.example.wherenow.notification

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

interface TravelReminderService {
    fun scheduleReminder(tripId: String, tripDate: Long)
    fun cancelReminder(tripId: String)
}

class TravelReminderServiceImpl(private val context: Context) : TravelReminderService {
    override fun scheduleReminder(tripId: String, tripDate: Long) {
        val weekInMillis = 7 * 24 * 60 * 60 * 1000L
        val delay = tripDate - weekInMillis

        if (delay > 0) {
            val request = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            val uniqueWorkName = "push_reminder_trip_$tripId"

            WorkManager.getInstance(context).enqueueUniqueWork(
                uniqueWorkName = uniqueWorkName,
                existingWorkPolicy = ExistingWorkPolicy.REPLACE,
                request = request
            )
        } else {
            Log.d("TravelReminderService", "Trip too close or already passed — not scheduling push")
        }
    }

    override fun cancelReminder(tripId: String) {
        val uniqueWorkName = "push_reminder_trip_$tripId"
        WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        Log.d("TravelReminderService", "Cancelled push for tripDate=$tripId")
    }
}