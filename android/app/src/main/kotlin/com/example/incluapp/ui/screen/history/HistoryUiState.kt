package com.example.incluapp.ui.screen.history

import com.example.incluapp.domain.model.Reading

data class HistoryUiState(
    val readings     : List<Reading> = emptyList(),
    val isLoading    : Boolean       = false,
    val searchQuery  : String        = "",
    val errorMessage : String?       = null
) {
    val filteredReadings: List<Reading>
        get() = if (searchQuery.isBlank()) readings
                else readings.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    it.extractedText.contains(searchQuery, ignoreCase = true)
                }
}
