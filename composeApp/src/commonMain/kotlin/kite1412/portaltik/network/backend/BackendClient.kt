package kite1412.portaltik.network.backend

import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.fullPath
import kite1412.portaltik.Logger
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.ktorHttpClient
import kite1412.portaltik.network.backend.util.getPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object BackendClient : KoinComponent {
    const val LOG_TAG = "BackendClient"
    val dataStore: PortalTikDataStore by inject()
    val httpClient = ktorHttpClient

    suspend inline fun rawGet(
        path: String,
        useToken: Boolean = true,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient
        .get(getPath(path)) {
            if (useToken) attachToken()
            block()
        }
        .also(::log)

    suspend inline fun <reified Response> get(
        path: String,
        withToken: Boolean = true,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response = rawGet(path, withToken, block).body()

    suspend inline fun <reified Request : Any> rawPost(
        path: String,
        body: Request,
        useToken: Boolean = true,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.post(getPath(path)) {
        setBody(body)
        if (useToken) attachToken()
        block()
    }.also(::log)

    suspend inline fun <reified Request : Any, reified Response> post(
        path: String,
        body: Request,
        useToken: Boolean = true,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response = rawPost(
        path = path,
        body = body,
        useToken = useToken,
        block = block
    ).body()

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

    fun log(response: HttpResponse) {
        Logger.i(
            tag = LOG_TAG,
            message = "status ${response.request.url.fullPath}: ${response.status.value}"
        )
    }

    suspend fun HttpRequestBuilder.attachToken() {
        val token = dataStore.getAuthSession()?.token ?: throw IllegalStateException("Not Authenticated")
        headers {
            append("Authorization", "Bearer $token")
        }
    }
}