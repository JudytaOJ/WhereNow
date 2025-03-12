package com.example.wherenow.database.trip

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.sql.Date

@Entity(tableName = "trip_details")
data class Trip(
    @ColumnInfo("date")
    @TypeConverters(Converters::class)
    val date: Date?,
    @ColumnInfo("image")
    val image: Int,
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
    @ColumnInfo("distance")
    val distance: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}