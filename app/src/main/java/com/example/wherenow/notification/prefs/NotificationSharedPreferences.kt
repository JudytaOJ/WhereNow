package com.example.wherenow.notification.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class NotificationPermissionHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun hasSeenNotificationDialog(): Boolean {
        return prefs.getBoolean(KEY_SEEN_DIALOG, false)
    }

    fun setSeenNotificationDialog() {
        prefs.edit { putBoolean(KEY_SEEN_DIALOG, true) }
    }

    companion object {
        private const val PREF_NAME = "prefs"
        private const val KEY_SEEN_DIALOG = "seen_notification_dialog"
    }
}