package com.dario.kmp.feature.discover.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.presentation.ItemDetailSideEffect
import com.dario.kmp.feature.discover.presentation.ItemDetailUiEvent
import com.dario.kmp.feature.discover.presentation.ItemDetailUiState
import com.dario.kmp.feature.discover.presentation.ItemDetailViewModel
import com.dario.kmp.feature.discover.theme.DiscoverTheme
import com.dario.kmp.feature.discover.ui.components.ArticleContent
import com.dario.kmp.feature.discover.ui.components.AudioContent
import com.dario.kmp.feature.discover.ui.components.RecipeContent
import kotlinx.coroutines.flow.collectLatest

/**
 * Root composable for the Discover item-detail feature.
 * Works on Android, iOS, and Web — no platform-specific code.
 *
 * Usage:
 * ```kotlin
 * val vm = DiscoverModule.provideViewModel(baseUrl = "https://your-cms.example.com/")
 * ItemDetailScreen(viewModel = vm, itemType = ItemType.RECIPE, onClose = { navigateBack() })
 * ```
 */
@Composable
fun ItemDetailScreen(
    viewModel: ItemDetailViewModel,
    itemType: ItemType,
    onClose: () -> Unit,
) {
    DiscoverTheme {
        LaunchedEffect(itemType) {
            viewModel.onEvent(ItemDetailUiEvent.LoadContent(itemType))
        }
        LaunchedEffect(Unit) {
            viewModel.sideEffect.collectLatest { effect ->
                when (effect) {
                    is ItemDetailSideEffect.NavigateBack  -> onClose()
                    is ItemDetailSideEffect.ShowError     -> { /* TODO: surface via Snackbar */ }
                }
            }
        }

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        when (val state = uiState) {
            is ItemDetailUiState.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            is ItemDetailUiState.Success ->
                ItemDetailContent(
                    content = state.content,
                    onClose = { viewModel.onEvent(ItemDetailUiEvent.Close) },
                )
            is ItemDetailUiState.Error ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, style = MaterialTheme.typography.bodyLarge)
                }
        }
    }
}

@Composable
private fun ItemDetailContent(content: DetailContent, onClose: () -> Unit) {
    when (content) {
        is DetailContent.Recipe  -> RecipeContent(state = content, onClose = onClose)
        is DetailContent.Article -> ArticleContent(state = content, onClose = onClose)
        is DetailContent.Audio   -> AudioContent(state = content, onClose = onClose)
    }
}
