package com.example.incluapp.domain.repository

import com.example.incluapp.domain.model.Reading
import kotlinx.coroutines.flow.Flow

/** Contrato de dominio para el acceso al historial de lecturas. */
interface ReadingRepository {
    fun getAllReadings(): Flow<List<Reading>>
    suspend fun getReadingById(id: Long): Reading?
    suspend fun saveReading(reading: Reading): Long
    suspend fun deleteReading(id: Long)
    suspend fun getTotalCount(): Int
}
