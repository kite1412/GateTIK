package kite1412.portaltik.network.backend

import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kite1412.portaltik.ktorHttpClient
import kite1412.portaltik.network.backend.util.getPath

object BackendClient {
    val httpClient = ktorHttpClient

    suspend inline fun rawGet(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.get(getPath(path), block)

    suspend inline fun <reified Response> get(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response = rawGet(path, block).body()

    suspend inline fun <reified Request : Any> rawPost(
        path: String,
        body: Request,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.post(getPath(path)) {
        setBody(body)
        block()
    }

    suspend inline fun <reified Request : Any, reified Response> post(
        path: String,
        body: Request,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response = rawPost(path, body, block).body()

    suspend inline fun <reified Request : Any, reified Response> put(
        path: String,
        body: Request,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response {
        return httpClient
            .put(path) {
                setBody(body)
                block()
            }
            .body()
    }

    suspend inline fun <reified Response> delete(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response {
        return httpClient
            .delete(path, block)
            .body()
    }
}