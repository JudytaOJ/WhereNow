package com.example.wherenow.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_details")
data class Trip(
    @ColumnInfo("date")
    val date: String,
    @ColumnInfo("departureCity")
    val departureCity: String,
    @ColumnInfo("departureCountry")
    val departureCountry: String,
    @ColumnInfo("departureCodeAirport")
    val arrivalCodeAirport: String,
    @ColumnInfo("departureAirport")
    val departureAirport: String,
    @ColumnInfo("arrivalCity")
    val arrivalCity: String,
    @ColumnInfo("arrivalCountry")
    val arrivalCountry: String,
    @ColumnInfo("arrivalAirport")
    val arrivalAirport: String,
    @ColumnInfo("arrivalCodeAirport")
    val departureCodeAirport: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)