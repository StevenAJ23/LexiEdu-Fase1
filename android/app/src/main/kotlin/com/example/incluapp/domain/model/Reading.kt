package com.example.incluapp.domain.model

data class Reading(
    val id              : Long   = 0L,
    val extractedText   : String,
    val imagePath       : String,
    val processingTimeMs: Long   = 0L,
    val createdAt       : Long   = System.currentTimeMillis(),
    val title           : String
)
