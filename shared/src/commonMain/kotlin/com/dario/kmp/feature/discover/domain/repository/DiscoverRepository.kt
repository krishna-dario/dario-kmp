package com.dario.kmp.feature.discover.domain.repository

import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.ItemType

interface DiscoverRepository {
    suspend fun getItemDetail(type: ItemType): Result<DetailContent>
}
