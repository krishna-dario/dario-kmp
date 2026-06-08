package com.dario.kmp.feature.discover.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Wraps content with the Discover feature's [MaterialTheme] (typography + colours).
 * Apply at the root of every Discover entry-point composable.
 */
@Composable
fun DiscoverTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = discoverTypography(),
        content = content,
    )
}
