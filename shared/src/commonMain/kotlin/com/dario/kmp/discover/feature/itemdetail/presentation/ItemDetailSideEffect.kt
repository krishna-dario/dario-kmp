package com.dario.kmp.discover.feature.itemdetail.presentation

sealed class ItemDetailSideEffect {
    object NavigateBack : ItemDetailSideEffect()
    data class ShowError(val message: String) : ItemDetailSideEffect()
}
