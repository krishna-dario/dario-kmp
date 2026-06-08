package com.dario.kmp.discover.feature.itemdetail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.domain.usecase.GetItemDetailUseCase
import com.dario.kmp.discover.feature.itemdetail.presentation.ItemDetailSideEffect
import com.dario.kmp.discover.feature.itemdetail.presentation.ItemDetailUiEvent
import com.dario.kmp.discover.feature.itemdetail.presentation.ItemDetailUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * KMP-compatible ViewModel — no Hilt annotations.
 * Use [com.dario.kmp.discover.di.DiscoverModule.provideItemDetailViewModel]
 * or your DI framework of choice to instantiate.
 */
class ItemDetailViewModel(
    private val getItemDetailUseCase: GetItemDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemDetailUiState>(ItemDetailUiState.Loading)
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ItemDetailSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: ItemDetailUiEvent) {
        when (event) {
            is ItemDetailUiEvent.LoadContent -> loadContent(event.type)
            is ItemDetailUiEvent.FavoriteToggled -> { /* TODO */ }
            is ItemDetailUiEvent.Close -> sendEffect(ItemDetailSideEffect.NavigateBack)
        }
    }

    private fun loadContent(type: ItemType) {
        viewModelScope.launch {
            _uiState.value = ItemDetailUiState.Loading
            getItemDetailUseCase(type).fold(
                onSuccess = { content ->
                    _uiState.value = ItemDetailUiState.Success(content)
                },
                onFailure = { throwable ->
                    val message = throwable.message ?: "Something went wrong"
                    _uiState.value = ItemDetailUiState.Error(message)
                    sendEffect(ItemDetailSideEffect.ShowError(message))
                }
            )
        }
    }

    private fun sendEffect(effect: ItemDetailSideEffect) {
        viewModelScope.launch { _sideEffect.send(effect) }
    }
}
