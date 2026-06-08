package com.dario.kmp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates a configured [HttpClient] for all KMP targets.
 *
 * @param baseUrl  Base URL applied to every request via [defaultRequest].
 * @param enableLogging  Set to false in production to suppress body logging.
 */
fun createHttpClient(
    baseUrl: String,
    enableLogging: Boolean = true,
): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        })
    }
    if (enableLogging) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }
    }
    defaultRequest {
        url(baseUrl)
    }
}
