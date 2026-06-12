package com.example.incluapp.data.repository

import com.example.incluapp.data.local.dao.ReadingDao
import com.example.incluapp.data.local.entity.ReadingEntity
import com.example.incluapp.domain.model.Reading
import com.example.incluapp.domain.repository.ReadingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReadingRepositoryImpl(private val dao: ReadingDao) : ReadingRepository {

    override fun getAllReadings(): Flow<List<Reading>> =
        dao.getAllReadings().map { list -> list.map { it.toDomain() } }

    override suspend fun getReadingById(id: Long): Reading? =
        dao.getReadingById(id)?.toDomain()

    override suspend fun saveReading(reading: Reading): Long =
        dao.insertReading(reading.toEntity())

    override suspend fun deleteReading(id: Long) =
        dao.deleteReadingById(id)

    override suspend fun getTotalCount(): Int =
        dao.getTotalCount()

    // ── Mappers ────────────────────────────────────────────────────────────

    private fun ReadingEntity.toDomain() = Reading(
        id               = id,
        title            = title,
        extractedText    = extractedText,
        imagePath        = imagePath,
        processingTimeMs = processingTimeMs,
        createdAt        = createdAt
    )

    private fun Reading.toEntity() = ReadingEntity(
        id               = id,
        title            = title,
        extractedText    = extractedText,
        imagePath        = imagePath,
        processingTimeMs = processingTimeMs,
        createdAt        = createdAt
    )
}
