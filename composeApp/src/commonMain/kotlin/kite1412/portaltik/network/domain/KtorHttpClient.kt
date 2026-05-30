package kite1412.portaltik.network.domain

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kite1412.portaltik.BuildConfig
import kotlinx.serialization.json.Json

val ktorHttpClient: HttpClient = createHttpClient()

fun createHttpClient(
    engine: HttpClientEngineFactory<*> = CIO
): HttpClient =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        defaultRequest {
            url(BuildConfig.BACKEND_URL)
            contentType(ContentType.Application.Json)
        }
    }