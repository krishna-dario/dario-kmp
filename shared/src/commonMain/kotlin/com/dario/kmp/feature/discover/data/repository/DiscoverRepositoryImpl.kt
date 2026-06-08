package com.dario.kmp.feature.discover.data.repository

import com.dario.kmp.feature.discover.data.datasource.DiscoverRemoteDataSource
import com.dario.kmp.feature.discover.data.local.DiscoverDemoDataSource
import com.dario.kmp.feature.discover.domain.model.DetailContent
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.dario.kmp.feature.discover.domain.repository.DiscoverRepository

internal class DiscoverRepositoryImpl(
    private val remoteDataSource: DiscoverRemoteDataSource,
) : DiscoverRepository {

    override suspend fun getItemDetail(type: ItemType): Result<DetailContent> {
        val remote = remoteDataSource.getItemDetail(type)
        // Fall back to bundled demo data until the CMS API is live.
        return if (remote.isSuccess) remote
        else Result.success(DiscoverDemoDataSource.getContent(type))
    }
}
