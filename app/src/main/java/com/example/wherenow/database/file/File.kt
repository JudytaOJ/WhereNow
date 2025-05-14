package com.example.wherenow.database.file

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class File(
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("tripId")
    val tripId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)