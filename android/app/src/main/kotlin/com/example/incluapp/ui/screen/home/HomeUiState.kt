package com.example.incluapp.ui.screen.home

data class HomeUiState(
    val isLoading            : Boolean = false,
    val showPermissionDialog : Boolean = false,
    val recentPreview        : String? = null,
    val totalReadings        : Int     = 0,
    val errorMessage         : String? = null
)

sealed interface HomeUiEvent {
    data object CapturePhoto      : HomeUiEvent
    data object PickFromGallery   : HomeUiEvent
    data object DismissError      : HomeUiEvent
    data object DismissPermission : HomeUiEvent
    data class  ImageSelected(val path: String) : HomeUiEvent
}
