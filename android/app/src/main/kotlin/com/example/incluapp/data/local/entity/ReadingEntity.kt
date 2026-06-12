package com.example.incluapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings")
data class ReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title           : String,
    val extractedText   : String,
    val imagePath       : String,
    val processingTimeMs: Long,
    val createdAt       : Long
)
