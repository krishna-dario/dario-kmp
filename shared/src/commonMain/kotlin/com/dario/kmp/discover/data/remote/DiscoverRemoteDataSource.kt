package com.dario.kmp.discover.data.remote

import com.dario.kmp.discover.data.remote.dto.ItemDetailMapper
import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.ItemType

class DiscoverRemoteDataSource(
    private val apiClient: DiscoverApiClient
) {
    suspend fun getItemDetail(type: ItemType): Result<DetailContent> =
        runCatching {
            ItemDetailMapper.toDomain(apiClient.getItemDetail(type.name))
        }
}
