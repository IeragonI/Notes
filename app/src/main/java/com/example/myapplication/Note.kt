package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val tags: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
