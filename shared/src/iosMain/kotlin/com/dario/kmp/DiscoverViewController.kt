package com.dario.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.dario.kmp.discover.di.DiscoverModule
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.feature.itemdetail.ui.ItemDetailRoute

/**
 * Returns a UIViewController hosting the Discover item-detail screen.
 *
 * Called from Swift:
 *   let vc = DiscoverViewControllerKt.DiscoverViewController(type: "RECIPE") { self.dismiss(animated: true) }
 *   present(vc, animated: true)
 *
 * @param type  "RECIPE" | "ARTICLE" | "AUDIO"  (matches ItemType enum name)
 * @param baseUrl  CMS base URL (replace with real value)
 * @param onClose  called when the user taps the close/back button
 */
fun DiscoverViewController(
    type: String,
    baseUrl: String = "https://api.example.com/", // TODO: replace with real CMS URL
    onClose: () -> Unit
) = ComposeUIViewController {
    val itemType = runCatching { ItemType.valueOf(type) }.getOrElse { ItemType.RECIPE }
    val viewModel = DiscoverModule.provideItemDetailViewModel(baseUrl = baseUrl)
    ItemDetailRoute(viewModel = viewModel, itemType = itemType, onClose = onClose)
}
