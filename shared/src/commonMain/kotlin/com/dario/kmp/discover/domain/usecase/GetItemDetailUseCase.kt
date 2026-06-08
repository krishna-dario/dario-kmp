package com.dario.kmp.discover.domain.usecase

import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.domain.repository.IDiscoverRepository

class GetItemDetailUseCase(
    private val repository: IDiscoverRepository
) {
    suspend operator fun invoke(type: ItemType): Result<DetailContent> =
        repository.getItemDetail(type)
}
