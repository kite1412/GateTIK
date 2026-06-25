package kite1412.gatetik

import android.webkit.PermissionRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.AccompanistWebChromeClient
import com.multiplatform.webview.web.PlatformWebViewParams
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@Composable
actual fun WebRtcPlayer(url: String, modifier: Modifier) {
    val state = rememberWebViewState(url)
    state.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
        }
    }

    val chromeClient = remember(state) {
        object : AccompanistWebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        }
    }

    WebView(
        state = state,
        modifier = modifier,
        onDispose = { nativeWebView ->
            nativeWebView.evaluateJavascript("""
                (async () => {
                    try {
                        document.querySelectorAll('*').forEach(el => {
                            if (el.srcObject) {
                                el.srcObject.getTracks().forEach(t => t.stop());
                                el.srcObject = null;
                            }
                        });
                    } catch(e) {}
                })();
            """.trimIndent(), null)
            nativeWebView.loadUrl("about:blank")
            nativeWebView.clearHistory()
        },
        platformWebViewParams = PlatformWebViewParams(
            chromeClient = chromeClient
        )
    )
}
