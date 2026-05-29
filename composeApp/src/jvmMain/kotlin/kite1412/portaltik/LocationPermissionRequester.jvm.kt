package kite1412.portaltik

import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocationPermissionRequester(
    onResult: (granted: Boolean) -> Unit
): () -> Unit = {}