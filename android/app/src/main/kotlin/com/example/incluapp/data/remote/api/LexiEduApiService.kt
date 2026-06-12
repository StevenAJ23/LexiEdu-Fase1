package com.example.incluapp.data.remote.api

import com.example.incluapp.data.remote.dto.AccessibilityTipDto
import com.example.incluapp.data.remote.dto.TextEnhancementRequest
import com.example.incluapp.data.remote.dto.TextEnhancementResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LexiEduApiService {

    /** Envía texto extraído y recibe una versión simplificada con oraciones clave. */
    @POST("api/v1/enhance-text")
    suspend fun enhanceText(
        @Body request: TextEnhancementRequest
    ): Response<TextEnhancementResponse>

    /** Recupera tips de accesibilidad para la pantalla de ayuda. */
    @GET("api/v1/accessibility-tips")
    suspend fun getAccessibilityTips(): Response<List<AccessibilityTipDto>>
}
