package kite1412.portaltik

import androidx.compose.runtime.Composable

@Composable
actual fun rememberNotificationPermissionRequester(onResult: (Boolean) -> Unit): () -> Unit = {}