package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drafts")
data class Draft(
    @PrimaryKey val id: Int = 1,
    val title: String,
    val body: String,
    val tags: List<String> = emptyList()
)
