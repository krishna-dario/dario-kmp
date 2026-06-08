package com.dario.kmp.feature.discover.domain.model

import androidx.compose.runtime.Immutable

/**
 * Domain model for Discover item-detail content.
 *
 * Image resolution priority (handled by UI layer):
 *  1. [imageUrl]  — remote URL via Coil AsyncImage (live API)
 *  2. [imageKey]  — bundled composeResources drawable (demo / offline fallback)
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
        val nextUp: List<NextUpItem>,
    ) : DetailContent

    data class Article(
        val imageUrl: String?,
        val imageKey: DiscoverImageKey?,
        val title: String,
        val body: String,
        val topics: List<String>,
        val nextUp: List<NextUpItem>,
    ) : DetailContent

    data class Audio(
        val imageUrl: String?,
        val imageKey: DiscoverImageKey?,
        val title: String,
        val description: String,
        val duration: String,
        val topics: List<String>,
        val nextUp: List<NextUpItem>,
    ) : DetailContent
}
