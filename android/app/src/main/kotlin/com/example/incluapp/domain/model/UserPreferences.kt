package com.example.incluapp.domain.model

data class UserPreferences(
    val speechRate          : Float   = 0.5f,
    val speechPitch         : Float   = 1.0f,
    val fontSize            : Float   = 16f,
    val highContrastEnabled : Boolean = false
)
