package kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kite1412.gatetik.Logger
import org.jetbrains.skia.Image

val LocalRemoteImageResolver = compositionLocalOf<RemoteImageResolver> {
    throw NotImplementedError("RemoteImageResolver is not initialized")
}

fun interface RemoteImageResolver {
    suspend fun resolve(payload: Any): ByteArray?
}

@Composable
fun rememberRemoteImageLoader(payload: Any, vararg keys: Any?) = remember(payload, keys) {
    RemoteImageLoader(payload)
}

class RemoteImageLoader(
    private val payload: Any
) {
    suspend fun resolveWith(
        resolver: RemoteImageResolver
    ): ImageBitmap? = try {
        val content = resolver.resolve(payload) ?: return null

        Image
            .makeFromEncoded(content)
            .toComposeImageBitmap()
    } catch (e: Exception) {
        Logger.e(
            tag = "RemoteImageResolver",
            message = "Failed to resolve image",
            throwable = e
        )
        null
    }
}