package com.dario.kmp.feature.discover.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDetailResponse(
    @SerialName("type") val type: String,
    @SerialName("recipe") val recipe: RecipeDto? = null,
    @SerialName("article") val article: ArticleDto? = null,
    @SerialName("audio") val audio: AudioDto? = null,
    @SerialName("nextUp") val nextUp: List<NextUpDto> = emptyList(),
)

@Serializable
data class RecipeDto(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("servings") val servings: String,
    @SerialName("duration") val duration: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("ingredients") val ingredients: List<String>,
    @SerialName("instructions") val instructions: List<String>,
    @SerialName("nutrition") val nutrition: List<NutritionDto>,
    @SerialName("topics") val topics: List<String>,
    @SerialName("heroImageUrl") val heroImageUrl: String? = null,
)

@Serializable
data class ArticleDto(
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("topics") val topics: List<String>,
    @SerialName("imageUrl") val imageUrl: String? = null,
)

@Serializable
data class AudioDto(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("duration") val duration: String,
    @SerialName("topics") val topics: List<String>,
    @SerialName("imageUrl") val imageUrl: String? = null,
)

@Serializable
data class NutritionDto(
    @SerialName("label") val label: String,
    @SerialName("value") val value: String,
)

@Serializable
data class NextUpDto(
    @SerialName("title") val title: String,
    @SerialName("type") val type: String,
    @SerialName("imageUrl") val imageUrl: String? = null,
)
