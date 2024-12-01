package com.example.wherenow.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_details")
data class Trip(
    @ColumnInfo("date")
    val date: String,
    @ColumnInfo("cityFrom")
    val cityFrom: String,
    @ColumnInfo("countryFrom")
    val countryFrom: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)