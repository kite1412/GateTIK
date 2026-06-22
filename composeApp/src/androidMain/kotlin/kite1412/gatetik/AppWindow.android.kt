package kite1412.gatetik

import androidx.compose.runtime.Composable

@Composable
actual fun AppWindow(
    title: String,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {}