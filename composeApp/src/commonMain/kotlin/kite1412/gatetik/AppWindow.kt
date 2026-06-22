package kite1412.gatetik

import androidx.compose.runtime.Composable

@Composable
expect fun AppWindow(title: String, onClose: () -> Unit, content: @Composable () -> Unit)