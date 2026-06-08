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
 * let vc = DiscoverViewControllerKt.DiscoverViewController(
 *     type: "RECIPE",
 *     baseUrl: "https://your-cms.example.com/"
 * ) { self.dismiss(animated: true) }
 * present(vc, animated: true)
 * ```
 *
 * @param type     One of `"RECIPE"`, `"ARTICLE"`, `"AUDIO"` (matches [ItemType] name).
 * @param baseUrl  CMS base URL — replace with real value before shipping.
 * @param onClose  Called when the user taps the close / back button.
 */
fun DiscoverViewController(
    type: String,
    baseUrl: String,
    onClose: () -> Unit,
) = ComposeUIViewController {
    val itemType = runCatching { ItemType.valueOf(type.uppercase()) }
        .getOrDefault(ItemType.RECIPE)
    val viewModel = DiscoverModule.provideViewModel(baseUrl = baseUrl)
    ItemDetailScreen(viewModel = viewModel, itemType = itemType, onClose = onClose)
}
