package com.example.incluapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TextEnhancementRequest(
    @SerializedName("text")     val text    : String,
    @SerializedName("language") val language: String = "es"
)

data class TextEnhancementResponse(
    @SerializedName("original_text")   val originalText  : String,
    @SerializedName("simplified_text") val simplifiedText: String,
    @SerializedName("key_sentences")   val keySentences  : List<String>,
    @SerializedName("reading_level")   val readingLevel  : String
)

data class AccessibilityTipDto(
    @SerializedName("id")       val id      : Int,
    @SerializedName("title")    val title   : String,
    @SerializedName("content")  val content : String,
    @SerializedName("category") val category: String
)
