package com.example.incluapp.ui.screen.reader

data class ReaderUiState(
    val extractedText  : String  = "",
    val isSpeaking     : Boolean = false,
    val speechRate     : Float   = 0.5f,
    val fontSize       : Float   = 16f,
    val isLoading      : Boolean = false,
    val isSaved        : Boolean = false,
    val errorMessage   : String? = null
)

sealed interface ReaderUiEvent {
    data object TogglePlayback                  : ReaderUiEvent
    data object StopPlayback                    : ReaderUiEvent
    data object SaveReading                     : ReaderUiEvent
    data class  ChangeSpeechRate(val rate: Float) : ReaderUiEvent
    data class  ChangeFontSize(val size: Float)   : ReaderUiEvent
}
