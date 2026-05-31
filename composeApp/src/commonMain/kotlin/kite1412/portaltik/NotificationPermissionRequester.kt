package kite1412.portaltik

import androidx.compose.runtime.Composable

@Composable
expect fun rememberNotificationPermissionRequester(
    onResult: (Boolean) -> Unit
): () -> Unit