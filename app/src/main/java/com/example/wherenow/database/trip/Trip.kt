package com.example.wherenow.database.trip

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_details")
data class Trip(
    @ColumnInfo("date")
    val date: Long,
    @ColumnInfo("image")
    val image: Int,
    @ColumnInfo("departureCity")
    val departureCity: String,
    @ColumnInfo("departureCountry")
    val departureCountry: String,
    @ColumnInfo("arrivalCodeAirport")
    val arrivalCodeAirport: String,
    @ColumnInfo("departureAirport")
    val departureAirport: String,
    @ColumnInfo("arrivalCity")
    val arrivalCity: String,
    @ColumnInfo("arrivalCountry")
    val arrivalCountry: String,
    @ColumnInfo("arrivalAirport")
    val arrivalAirport: String,
    @ColumnInfo("departureCodeAirport")
    val departureCodeAirport: String,
    @ColumnInfo("distance")
    val distance: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)