package com.example.incluapp.navigation

import kotlinx.serialization.Serializable

/** Destino: pantalla de bienvenida animada. */
@Serializable
object Splash

/** Destino: pantalla principal (cámara / galería). */
@Serializable
object Home

/**
 * Destino: lector de texto con TTS.
 * @param readingId  ID del historial para reabrir una lectura guardada (-1 si es nueva).
 * @param imagePath  Ruta local de la imagen capturada (vacío si se abre desde historial).
 */
@Serializable
data class Reader(
    val readingId: Long   = -1L,
    val imagePath: String = ""
)

/**
 * Destino: historial de lecturas guardadas en Room.
 * @param auditHash  Hash de sesión único generado en la pantalla de origen
 *                   para auditoría de inmutabilidad.
 */
@Serializable
data class History(val auditHash: String)

/** Destino: pantalla de ayuda y preguntas frecuentes. */
@Serializable
object Help
