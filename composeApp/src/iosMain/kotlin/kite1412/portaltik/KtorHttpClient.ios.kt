package kite1412.portaltik

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual val ktorHttpClient: HttpClient = createHttpClient(Darwin)