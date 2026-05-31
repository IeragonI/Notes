package com.example.myapplication

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return if (value.isBlank()) emptyList() else value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.filter { it.isNotBlank() }.joinToString(",")
    }
}
