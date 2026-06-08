package com.dario.kmp.discover.feature.itemdetail.presentation

import com.dario.kmp.discover.domain.model.DetailContent

sealed class ItemDetailUiState {
    object Loading : ItemDetailUiState()
    data class Success(val content: DetailContent) : ItemDetailUiState()
    data class Error(val message: String) : ItemDetailUiState()
}
