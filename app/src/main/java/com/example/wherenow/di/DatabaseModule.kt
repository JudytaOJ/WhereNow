package com.example.wherenow.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wherenow.database.calendar.CalendarDatabase
import com.example.wherenow.database.file.FileDatabase
import com.example.wherenow.database.notes.NoteDatabase
import com.example.wherenow.database.trip.TripDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    //database
    single<TripDatabase> {
        Room.databaseBuilder(
            androidContext(),
            TripDatabase::class.java,
            androidContext().getDatabasePath("trip_database.db").absolutePath
        ).build()
    }

    single<NoteDatabase> {
        Room.databaseBuilder(
            androidContext(),
            NoteDatabase::class.java,
            androidContext().getDatabasePath("notes_database.db").absolutePath
        ).build()
    }

    val migration3To4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE file ADD COLUMN tripId INTEGER DEFAULT 0 NOT NULL")
        }
    }

    single<FileDatabase> {
        Room.databaseBuilder(
            androidContext(),
            FileDatabase::class.java,
            androidContext().getDatabasePath("file_database.db").absolutePath
        )
            .addMigrations(migration3To4)
            .build()
    }

    single<CalendarDatabase> {
        Room.databaseBuilder(
            androidContext(),
            CalendarDatabase::class.java,
            androidContext().getDatabasePath("calendar_database.db").absolutePath
        ).build()
    }
}