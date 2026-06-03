package kite1412.gatetik

import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocationPermissionRequester(
    onResult: (granted: Boolean) -> Unit
): () -> Unit = {}