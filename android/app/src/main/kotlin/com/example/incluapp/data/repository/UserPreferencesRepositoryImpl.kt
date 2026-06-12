package com.example.incluapp.data.repository

import com.example.incluapp.data.local.dao.UserPreferencesDao
import com.example.incluapp.data.local.entity.UserPreferencesEntity
import com.example.incluapp.domain.model.UserPreferences
import com.example.incluapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val dao: UserPreferencesDao
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> =
        dao.getUserPreferences().map { entity -> entity?.toDomain() ?: UserPreferences() }

    override suspend fun saveUserPreferences(preferences: UserPreferences) =
        dao.saveUserPreferences(preferences.toEntity())

    override suspend fun updateSpeechRate(rate: Float) =
        dao.updateSpeechRate(rate)

    override suspend fun updateFontSize(size: Float) =
        dao.updateFontSize(size)

    // ── Mappers ────────────────────────────────────────────────────────────

    private fun UserPreferencesEntity.toDomain() = UserPreferences(
        speechRate          = speechRate,
        speechPitch         = speechPitch,
        fontSize            = fontSize,
        highContrastEnabled = highContrastEnabled
    )

    private fun UserPreferences.toEntity() = UserPreferencesEntity(
        speechRate          = speechRate,
        speechPitch         = speechPitch,
        fontSize            = fontSize,
        highContrastEnabled = highContrastEnabled
    )
}
