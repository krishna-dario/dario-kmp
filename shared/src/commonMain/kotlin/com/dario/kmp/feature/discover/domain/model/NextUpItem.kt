package com.dario.kmp.feature.discover.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class NextUpItem(
    val title: String,
    val type: String,
    val imageUrl: String?,
    val imageKey: DiscoverImageKey? = null,
)
