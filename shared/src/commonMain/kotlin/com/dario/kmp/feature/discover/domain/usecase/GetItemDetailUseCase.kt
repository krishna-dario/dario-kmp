package com.dario.kmp.feature.discover.domain.usecase

import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.domain.repository.DiscoverRepository

class GetItemDetailUseCase(
    private val repository: DiscoverRepository,
) {
    suspend operator fun invoke(type: ItemType): Result<DetailContent> =
        repository.getItemDetail(type)
}
