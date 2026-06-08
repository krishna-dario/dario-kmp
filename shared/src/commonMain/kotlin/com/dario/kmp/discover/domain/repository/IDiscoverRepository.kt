package com.dario.kmp.discover.domain.repository

import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.ItemType

interface IDiscoverRepository {
    suspend fun getItemDetail(type: ItemType): Result<DetailContent>
}
