package kite1412.gatetik

import androidx.compose.runtime.Composable

@Composable
actual fun rememberNotificationPermissionRequester(onResult: (Boolean) -> Unit): () -> Unit = {}