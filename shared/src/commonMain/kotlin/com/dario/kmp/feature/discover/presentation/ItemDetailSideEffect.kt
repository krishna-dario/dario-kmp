package com.dario.kmp.feature.discover.presentation

sealed class ItemDetailSideEffect {
    data object NavigateBack : ItemDetailSideEffect()
    data class ShowError(val message: String) : ItemDetailSideEffect()
}
