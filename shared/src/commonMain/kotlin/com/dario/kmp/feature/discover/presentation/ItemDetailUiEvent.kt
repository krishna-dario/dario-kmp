package com.dario.kmp.feature.discover.presentation

import com.dario.kmp.feature.discover.domain.model.ItemType

sealed class ItemDetailUiEvent {
    data class LoadContent(val type: ItemType) : ItemDetailUiEvent()
    data object FavoriteToggled : ItemDetailUiEvent()
    data object Close : ItemDetailUiEvent()
}
