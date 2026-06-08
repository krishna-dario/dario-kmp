package com.dario.kmp.feature.discover.data.datasource

import com.dario.kmp.feature.discover.data.remote.api.DiscoverApi
import com.dario.kmp.feature.discover.data.remote.dto.ItemDetailMapper
import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.ItemType

internal class DiscoverRemoteDataSource(private val api: DiscoverApi) {

    suspend fun getItemDetail(type: ItemType): Result<DetailContent> =
        runCatching {
            ItemDetailMapper.toDomain(api.getItemDetail(type.name))
        }
}
