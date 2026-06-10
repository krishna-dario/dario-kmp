package com.dario.kmp.feature.discover

import androidx.compose.ui.window.ComposeUIViewController
import com.dario.kmp.feature.discover.di.DiscoverModule
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.ui.ItemDetailScreen

/**
 * Returns a [UIViewController] hosting the Discover item-detail screen.
 *
 * Called from Swift:
 * ```swift
 * // Live API
 * let vc = DiscoverViewControllerKt.DiscoverViewController(
 *     type: "RECIPE",
 *     baseUrl: "https://your-cms.example.com/",
 *     skipApiCall: false
 * ) { self.dismiss(animated: true) }
 *
 * // Dummy data (no network call)
 * let vc = DiscoverViewControllerKt.DiscoverViewController(
 *     type: "RECIPE",
 *     baseUrl: "",
 *     skipApiCall: true
 * ) { self.dismiss(animated: true) }
 * ```
 *
 * @param type        One of `"RECIPE"`, `"ARTICLE"`, `"AUDIO"` (matches [ItemType] name).
 * @param baseUrl     CMS base URL. Pass empty string when [skipApiCall] is true.
 * @param skipApiCall If true, always returns bundled demo data — no network call is made.
 * @param onClose     Called when the user taps the close / back button.
 */
fun DiscoverViewController(
    type: String,
    baseUrl: String,
    skipApiCall: Boolean = false,
    onClose: () -> Unit,
) = ComposeUIViewController {
    val itemType = runCatching { ItemType.valueOf(type.uppercase()) }
        .getOrDefault(ItemType.RECIPE)
    val viewModel = DiscoverModule.provideViewModel(baseUrl = baseUrl, skipApiCall = skipApiCall)
    ItemDetailScreen(viewModel = viewModel, itemType = itemType, onClose = onClose)
}
