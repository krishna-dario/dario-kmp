package com.dario.kmp.discover.domain.model

/**
 * Identifies a bundled local image in composeResources/drawable/.
 * Used as a fallback when [DetailContent] has no remote [imageUrl].
 * The UI layer resolves this to the actual [org.jetbrains.compose.resources.DrawableResource].
 */
enum class DiscoverImageKey {
    RECIPE_BANNER,
    ARTICLE,
    AUDIO,
    RECIPE_1,
    RECIPE_2
}
