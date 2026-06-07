package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.RemoteImageResolver
import org.jetbrains.skia.Image

@Composable
actual fun rememberRemoteImageLoader(
    payload: Any,
    vararg keys: Any?
): RemoteImageLoader = remember(payload, keys) {
    JvmRemoteImageLoader(payload)
}

class JvmRemoteImageLoader(
    private val payload: Any
) : RemoteImageLoader {
    override suspend fun loadWith(
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