package kite1412.gatetik

import androidx.compose.runtime.Composable

@Composable
expect fun rememberNotificationPermissionRequester(
    onResult: (Boolean) -> Unit
): () -> Unit