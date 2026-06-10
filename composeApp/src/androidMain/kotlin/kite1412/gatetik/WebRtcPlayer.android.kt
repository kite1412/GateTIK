package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@Composable
actual fun WebRtcPlayer(url: String, modifier: Modifier) {
    val state = rememberWebViewState(url)

    WebView(
        state = state,
        modifier = modifier
    )
}