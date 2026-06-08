package com.dario.kmp.feature.discover.presentation

import com.dario.kmp.feature.discover.domain.model.DetailContent

sealed class ItemDetailUiState {
    data object Loading : ItemDetailUiState()
    data class Success(val content: DetailContent) : ItemDetailUiState()
    data class Error(val message: String) : ItemDetailUiState()
}
