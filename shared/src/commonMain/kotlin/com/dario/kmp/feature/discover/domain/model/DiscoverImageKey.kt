package com.dario.kmp.feature.discover.domain.model

/**
 * Identifies a bundled composeResources drawable.
 * Used as an offline / demo fallback when no remote [imageUrl] is available.
 * The UI layer resolves this key to the actual [DrawableResource].
 */
enum class DiscoverImageKey {
    RECIPE_BANNER,
    ARTICLE,
    AUDIO,
    RECIPE_1,
    RECIPE_2,
}
