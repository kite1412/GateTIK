package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.RemoteImageResolver

@Composable
expect fun rememberRemoteImageLoader(payload: Any, vararg keys: Any?): RemoteImageLoader

interface RemoteImageLoader {
    suspend fun loadWith(resolver: RemoteImageResolver): ImageBitmap?
}