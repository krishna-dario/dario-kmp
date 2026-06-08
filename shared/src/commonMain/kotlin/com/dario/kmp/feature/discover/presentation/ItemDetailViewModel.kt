package com.dario.kmp.feature.discover.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.domain.usecase.GetItemDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * KMP-compatible ViewModel — no Hilt annotations.
 *
 * Instantiate via [com.dario.kmp.feature.discover.di.DiscoverModule.provideViewModel]
 * or wire through your DI framework of choice.
 */
class ItemDetailViewModel(
    private val getItemDetail: GetItemDetailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemDetailUiState>(ItemDetailUiState.Loading)
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ItemDetailSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: ItemDetailUiEvent) {
        when (event) {
            is ItemDetailUiEvent.LoadContent   -> loadContent(event.type)
            is ItemDetailUiEvent.FavoriteToggled -> { /* TODO: persist favourite */ }
            is ItemDetailUiEvent.Close          -> emit(ItemDetailSideEffect.NavigateBack)
        }
    }

    private fun loadContent(type: ItemType) {
        viewModelScope.launch {
            _uiState.value = ItemDetailUiState.Loading
            getItemDetail(type).fold(
                onSuccess = { _uiState.value = ItemDetailUiState.Success(it) },
                onFailure = { t ->
                    val msg = t.message ?: "Something went wrong"
                    _uiState.value = ItemDetailUiState.Error(msg)
                    emit(ItemDetailSideEffect.ShowError(msg))
                },
            )
        }
    }

    private fun emit(effect: ItemDetailSideEffect) {
        viewModelScope.launch { _sideEffect.send(effect) }
    }
}
