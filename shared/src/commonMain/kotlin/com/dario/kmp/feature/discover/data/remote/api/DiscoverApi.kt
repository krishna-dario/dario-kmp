package com.dario.kmp.feature.discover.data.remote.api

import com.dario.kmp.feature.discover.data.remote.dto.ItemDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class DiscoverApi(private val httpClient: HttpClient) {

    suspend fun getItemDetail(type: String): ItemDetailResponse =
        httpClient.get("discover/item") {
            parameter("type", type)
        }.body()
}
