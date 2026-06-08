package com.dario.kmp.discover.data.repository

import com.dario.kmp.discover.data.DemoDataProvider
import com.dario.kmp.discover.data.remote.DiscoverRemoteDataSource
import com.dario.kmp.discover.domain.model.DetailContent
import com.dario.kmp.discover.domain.model.ItemType
import com.dario.kmp.discover.domain.repository.IDiscoverRepository

class DiscoverRepositoryImpl(
    private val remoteDataSource: DiscoverRemoteDataSource
) : IDiscoverRepository {

    override suspend fun getItemDetail(type: ItemType): Result<DetailContent> {
        val remote = remoteDataSource.getItemDetail(type)
        // Fall back to demo data until the CMS API is live.
        return if (remote.isSuccess) remote
        else Result.success(DemoDataProvider.getContent(type))
    }
}
