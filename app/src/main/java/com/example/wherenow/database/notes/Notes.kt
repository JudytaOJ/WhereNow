package com.example.wherenow.database.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)