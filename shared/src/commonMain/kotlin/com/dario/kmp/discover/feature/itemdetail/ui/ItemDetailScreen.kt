package com.dario.kmp.discover.feature.itemdetail.ui

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
import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.feature.itemdetail.presentation.ItemDetailSideEffect
import com.dario.kmp.discover.feature.itemdetail.presentation.ItemDetailUiEvent
import com.dario.kmp.discover.feature.itemdetail.presentation.ItemDetailUiState
import com.dario.kmp.discover.feature.itemdetail.presentation.viewmodel.ItemDetailViewModel
import com.dario.kmp.discover.theme.DiscoverTheme
import kotlinx.coroutines.flow.collectLatest

/**
 * Root composable for the Discover item-detail feature.
 * Works on Android, iOS, Web — no platform-specific code.
 *
 * Usage:
 * ```
 * val vm = DiscoverModule.provideItemDetailViewModel(baseUrl = "https://api.example.com/")
 * ItemDetailRoute(viewModel = vm, itemType = ItemType.RECIPE, onClose = { /* navigate back */ })
 * ```
 */
@Composable
fun ItemDetailRoute(
    viewModel: ItemDetailViewModel,
    itemType: ItemType,
    onClose: () -> Unit
) {
    DiscoverTheme {
        LaunchedEffect(itemType) {
            viewModel.onEvent(ItemDetailUiEvent.LoadContent(itemType))
        }

        LaunchedEffect(Unit) {
            viewModel.sideEffect.collectLatest { effect ->
                when (effect) {
                    is ItemDetailSideEffect.NavigateBack -> onClose()
                    is ItemDetailSideEffect.ShowError -> { /* TODO: show Snackbar/Toast */ }
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
                    onClose = { viewModel.onEvent(ItemDetailUiEvent.Close) }
                )

            is ItemDetailUiState.Error ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, style = MaterialTheme.typography.bodyLarge)
                }
        }
    }
}

/**
 * Routes to the correct content composable based on [DetailContent] type.
 */
@Composable
fun ItemDetailContent(
    content: DetailContent,
    onClose: () -> Unit
) {
    when (content) {
        is DetailContent.Recipe  -> RecipeContent(state = content, onClose = onClose)
        is DetailContent.Article -> ArticleContent(state = content, onClose = onClose)
        is DetailContent.Audio   -> AudioContent(state = content, onClose = onClose)
    }
}
