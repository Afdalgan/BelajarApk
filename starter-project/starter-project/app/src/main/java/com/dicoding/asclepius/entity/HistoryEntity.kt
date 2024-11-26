package com.dicoding.asclepius.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String,
    val result: String,
    val confidence: Double,
    val timestamp: Long = System.currentTimeMillis()
)
