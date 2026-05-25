package kite1412.portaltik

import io.ktor.client.HttpClient

actual val ktorHttpClient: HttpClient = createHttpClient()