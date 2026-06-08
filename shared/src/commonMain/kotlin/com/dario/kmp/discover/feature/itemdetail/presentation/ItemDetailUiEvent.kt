package com.dario.kmp.discover.feature.itemdetail.presentation

import com.dario.kmp.discover.domain.model.ItemType

sealed class ItemDetailUiEvent {
    data class LoadContent(val type: ItemType) : ItemDetailUiEvent()
    object FavoriteToggled : ItemDetailUiEvent()
    object Close : ItemDetailUiEvent()
}
