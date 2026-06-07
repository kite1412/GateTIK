package kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal

import androidx.compose.runtime.compositionLocalOf

val LocalRemoteImageResolver = compositionLocalOf<RemoteImageResolver> {
    throw NotImplementedError("RemoteImageResolver is not initialized")
}

fun interface RemoteImageResolver {
    suspend fun resolve(payload: Any): ByteArray?
}