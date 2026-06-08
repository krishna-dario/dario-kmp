package com.dario.kmp.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dario.kmp.feature.discover.di.DiscoverModule
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.ui.ItemDetailScreen

/**
 * Android entry point for the KMP Discover item-detail screen.
 *
 * Launch via the companion [start] factory — identical pattern to the
 * original Android-only ItemDetailActivity.
 *
 * Example:
 * ```kotlin
 * DiscoverItemDetailActivity.start(context, ItemType.RECIPE)
 * ```
 */
class DiscoverItemDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val type = intent
            .getStringExtra(EXTRA_TYPE)
            ?.let { runCatching { ItemType.valueOf(it) }.getOrNull() }
            ?: ItemType.RECIPE

        // Replace with your real base URL (or inject via BuildConfig / DI).
        val viewModel = DiscoverModule.provideViewModel(
            baseUrl = BASE_URL
        )

        setContent {
            ItemDetailScreen(
                viewModel = viewModel,
                itemType = type,
                onClose = ::finish
            )
        }
    }

    companion object {
        private const val EXTRA_TYPE = "extra_type"

        // TODO: replace with your real CMS base URL or inject via BuildConfig
        private const val BASE_URL = "https://api.example.com/"

        fun start(context: Context, type: ItemType) {
            context.startActivity(
                Intent(context, DiscoverItemDetailActivity::class.java).apply {
                    putExtra(EXTRA_TYPE, type.name)
                }
            )
        }
    }
}
