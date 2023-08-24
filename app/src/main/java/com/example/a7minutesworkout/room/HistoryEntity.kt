package com.example.a7minutesworkout.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history-table")

data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "IBM")var ibm: String = "",
    @ColumnInfo(name = "Category")var category: String = "",
    @ColumnInfo(name = "Date")var date: String = ""
)
