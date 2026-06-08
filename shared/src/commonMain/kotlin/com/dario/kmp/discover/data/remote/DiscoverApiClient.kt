package com.dario.kmp.discover.data.remote

import com.dario.kmp.discover.data.remote.dto.ItemDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Ktor-based API client replacing Android's Retrofit [DiscoverApiService].
 * Works on all KMP targets (Android, iOS, JS, WASM).
 */
class DiscoverApiClient(private val httpClient: HttpClient) {

    suspend fun getItemDetail(type: String): ItemDetailResponse =
        httpClient.get("discover/item") {
            parameter("type", type)
        }.body()
}
