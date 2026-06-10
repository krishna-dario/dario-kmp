package com.dario.kmp.feature.discover.di

import com.dario.kmp.core.network.createHttpClient
import com.dario.kmp.feature.discover.data.datasource.DiscoverRemoteDataSource
import com.dario.kmp.feature.discover.data.remote.api.DiscoverApi
import com.dario.kmp.feature.discover.data.repository.DiscoverRepositoryImpl
import com.dario.kmp.feature.discover.domain.repository.DiscoverRepository
import com.dario.kmp.feature.discover.domain.usecase.GetItemDetailUseCase
import com.dario.kmp.feature.discover.presentation.ItemDetailViewModel

/**
 * Manual dependency wiring for the Discover feature.
 *
 * Suitable for all KMP targets (no Hilt / no platform-specific DI).
 * Integrate with Koin or another DI framework as the project grows.
 *
 * Usage:
 * ```kotlin
 * // Live data from API:
 * val vm = DiscoverModule.provideViewModel(baseUrl = "https://your-cms.example.com/")
 *
 * // Always use bundled dummy data (no network call):
 * val vm = DiscoverModule.provideViewModel(baseUrl = "", skipApiCall = true)
 * ```
 */
object DiscoverModule {

    fun provideRepository(baseUrl: String, skipApiCall: Boolean = false): DiscoverRepository {
        val httpClient = createHttpClient(baseUrl)
        val api = DiscoverApi(httpClient)
        val remoteDataSource = DiscoverRemoteDataSource(api)
        return DiscoverRepositoryImpl(remoteDataSource, skipApiCall)
    }

    fun provideGetItemDetailUseCase(baseUrl: String, skipApiCall: Boolean = false): GetItemDetailUseCase =
        GetItemDetailUseCase(provideRepository(baseUrl, skipApiCall))

    fun provideViewModel(baseUrl: String, skipApiCall: Boolean = false): ItemDetailViewModel =
        ItemDetailViewModel(provideGetItemDetailUseCase(baseUrl, skipApiCall))
}
