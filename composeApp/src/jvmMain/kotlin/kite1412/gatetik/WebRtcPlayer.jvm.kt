package kite1412.gatetik

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import kite1412.gatetik.util.SupportedOS
import kite1412.gatetik.util.getSupportedOS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.EnumProgress
import org.cef.CefApp
import org.cef.CefClient
import org.cef.browser.CefBrowser
import java.awt.BorderLayout
import java.io.File
import javax.swing.JPanel
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

@Composable
actual fun WebRtcPlayer(url: String, modifier: Modifier) {
    var browser by remember { mutableStateOf<CefBrowser?>(null) }

    DisposableEffect(url) {
        val scope = CoroutineScope(Dispatchers.Swing)

        scope.launch {
            browser = CefBrowserProvider.createBrowser(url)
        }

        onDispose {
            val currentBrowser = browser ?: run {
                scope.cancel()
                return@onDispose
            }

            browser = null
            scope.cancel()

            CoroutineScope(Dispatchers.Swing + SupervisorJob()).launch {
                // Nuke all media streams — broader sweep than srcObject only
                currentBrowser.executeJavaScript("""
                    (async () => {
                        try {
                            const devices = await navigator.mediaDevices.enumerateDevices();
                            const stream = await navigator.mediaDevices.getUserMedia({ audio: true, video: true });
                            stream.getTracks().forEach(t => t.stop());
                        } catch(e) {}
                        document.querySelectorAll('*').forEach(el => {
                            if (el.srcObject) {
                                el.srcObject.getTracks().forEach(t => t.stop());
                                el.srcObject = null;
                            }
                        });
                    })();
                """.trimIndent(), currentBrowser.url, 0)

                delay(200.milliseconds)
                currentBrowser.loadURL("about:blank")
                delay(500.milliseconds)
                currentBrowser.close(true)
            }
        }
    }

    if (browser == null) Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    } else key(browser) {
        SwingPanel(
            modifier = modifier.fillMaxSize(),
            factory = {
                JPanel(BorderLayout()).apply {
                    browser
                        ?.uiComponent
                        ?.let { cefComponent ->
                            add(cefComponent, BorderLayout.CENTER)
                        }
                }
            }
        )
    }
}

object CefBrowserProvider {
    private val appMutex = Mutex()
    private val _loadProgress = MutableStateFlow(CefBrowserLoadProgress())
    val loadProgress = _loadProgress.asStateFlow()
    private var app: CefApp? = null
    private var client: CefClient? = null

    suspend fun initApp(): CefApp {
        app?.let { return it }

        return withContext(Dispatchers.IO) {
            appMutex.withLock {
                CefAppBuilder().apply {
                    val installDir = File(
                        System.getProperty("user.home"),
                        ".gatetik/jcef"
                    )
                    installDir.mkdirs()
                    setInstallDir(installDir)
                    setProgressHandler { status, progress ->
                        _loadProgress.value = CefBrowserLoadProgress(
                            when (status) {
                                EnumProgress.DOWNLOADING -> CefBrowserProgressStatus.DOWNLOADING
                                EnumProgress.INSTALL -> CefBrowserProgressStatus.INSTALL
                                EnumProgress.INITIALIZING -> CefBrowserProgressStatus.INITIALIZING
                                EnumProgress.INITIALIZED -> CefBrowserProgressStatus.INITIALIZED
                                else -> CefBrowserProgressStatus.LOCATING
                            },
                            percent = max(0f, progress)
                        )
                    }
                    addJcefArgs("--disable-web-security")
                    addJcefArgs("--enable-media-stream")
                    addJcefArgs("--ignore-certificate-errors")

                    if (getSupportedOS() == SupportedOS.WINDOWS) {
                        cefSettings.windowless_rendering_enabled = false

                        addJcefArgs("--autoplay-policy=no-user-gesture-required")
                    }
                }.build().also {
                    app = it
                }
            }
        }
    }

    suspend fun getClient(): CefClient {
        val app = initApp()

        return client ?: app.createClient().also { cefClient ->
            client = cefClient
        }
    }

    suspend fun createBrowser(url: String): CefBrowser =
        getClient().createBrowser(
            url,
            false,
            false
        )
}

data class CefBrowserLoadProgress(
    val status: CefBrowserProgressStatus = CefBrowserProgressStatus.LOCATING,
    val percent: Float = 0f // 0f - 100f
)

enum class CefBrowserProgressStatus {
    LOCATING,
    DOWNLOADING,
    INSTALL,
    INITIALIZING,
    INITIALIZED
}
