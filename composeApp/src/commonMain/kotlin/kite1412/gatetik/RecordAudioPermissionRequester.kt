package kite1412.gatetik

import androidx.compose.runtime.Composable

@Composable
expect fun rememberRecordAudioPermissionRequester(
    onResult: (granted: Boolean) -> Unit
): () -> Unit
