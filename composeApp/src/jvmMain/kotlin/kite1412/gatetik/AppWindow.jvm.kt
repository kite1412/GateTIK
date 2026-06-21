package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import java.awt.Dimension

@Composable
actual fun AppWindow(
    title: String,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Window(
        onCloseRequest = onClose,
        title = title
    ) {
        window.minimumSize = Dimension(800, 600)

        content()
    }
}