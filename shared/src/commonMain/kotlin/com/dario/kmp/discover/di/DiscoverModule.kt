package com.dario.kmp.discover.di

import com.dario.kmp.discover.data.remote.DiscoverApiClient
import com.dario.kmp.discover.data.remote.DiscoverRemoteDataSource
import com.dario.kmp.discover.data.repository.DiscoverRepositoryImpl
import com.dario.kmp.discover.domain.repository.IDiscoverRepository
import com.dario.kmp.discover.domain.usecase.GetItemDetailUseCase
import com.dario.kmp.discover.feature.itemdetail.presentation.viewmodel.ItemDetailViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Manual dependency wiring for the Discover feature.
 * Replaces Hilt modules — suitable for all KMP targets.
 * Integrate with your preferred DI framework (Koin, etc.) as the project grows.
 */
object DiscoverModule {

    fun provideHttpClient(baseUrl: String): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
        defaultRequest {
            url(baseUrl)
        }
    }

    fun provideRepository(baseUrl: String): IDiscoverRepository {
        val httpClient = provideHttpClient(baseUrl)
        val apiClient = DiscoverApiClient(httpClient)
        val remoteDataSource = DiscoverRemoteDataSource(apiClient)
        return DiscoverRepositoryImpl(remoteDataSource)
    }

    fun provideGetItemDetailUseCase(baseUrl: String): GetItemDetailUseCase =
        GetItemDetailUseCase(provideRepository(baseUrl))

    fun provideItemDetailViewModel(baseUrl: String): ItemDetailViewModel =
        ItemDetailViewModel(provideGetItemDetailUseCase(baseUrl))
}
