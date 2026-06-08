package com.dario.kmp.discover.domain.model

import androidx.compose.runtime.Immutable

/**
 * Domain model for item detail content.
 *
 * Image resolution priority (handled by the UI layer):
 *  1. [imageUrl] — remote URL loaded via Coil AsyncImage (API response)
 *  2. [imageKey] — bundled composeResources drawable (demo / offline fallback)
 *  3. Colour placeholder — when both are null
 */
@Immutable
sealed interface DetailContent {

    data class Recipe(
        val heroImageUrl: String?,
        val heroImageKey: DiscoverImageKey?,
        val title: String,
        val description: String,
        val servings: String,
        val duration: String,
        val tags: List<String>,
        val ingredients: List<String>,
        val instructions: List<String>,
        val nutrition: List<Pair<String, String>>,
        val topics: List<String>,
        val nextUp: List<NextUpItem>
    ) : DetailContent

    data class Article(
        val imageUrl: String?,
        val imageKey: DiscoverImageKey?,
        val title: String,
        val body: String,
        val topics: List<String>,
        val nextUp: List<NextUpItem>
    ) : DetailContent

    data class Audio(
        val imageUrl: String?,
        val imageKey: DiscoverImageKey?,
        val title: String,
        val description: String,
        val duration: String,
        val topics: List<String>,
        val nextUp: List<NextUpItem>
    ) : DetailContent
}

@Immutable
data class NextUpItem(
    val title: String,
    val type: String,
    val imageUrl: String?,
    val imageKey: DiscoverImageKey? = null
)
