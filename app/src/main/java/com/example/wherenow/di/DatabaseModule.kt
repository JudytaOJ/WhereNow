package com.example.wherenow.di

import androidx.room.Room
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

    single<FileDatabase> {
        Room.databaseBuilder(
            androidContext(),
            FileDatabase::class.java,
            androidContext().getDatabasePath("file_database.db").absolutePath
        ).build()
    }
}