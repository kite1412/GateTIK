package kite1412.gatetik

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.RemoteImageResolver

@Composable
actual fun rememberRemoteImageLoader(
    payload: Any,
    vararg keys: Any?
): RemoteImageLoader = remember(payload, keys) {
    AndroidRemoteImageLoader(payload)
}

class AndroidRemoteImageLoader(
    private val payload: Any
) : RemoteImageLoader {
    override suspend fun loadWith(
        resolver: RemoteImageResolver
    ): ImageBitmap? = try {
        val content = resolver.resolve(payload) ?: return null

        BitmapFactory
            .decodeByteArray(content, 0, content.size)
            .asImageBitmap()
    } catch (e: Exception) {
        Logger.e(
            tag = "RemoteImageResolver",
            message = "Failed to resolve image",
            throwable = e
        )
        null
    }
}