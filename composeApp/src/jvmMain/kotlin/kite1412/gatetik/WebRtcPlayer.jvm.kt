package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import me.friwi.jcefmaven.CefAppBuilder
import org.cef.CefApp
import org.cef.CefClient
import org.cef.browser.CefBrowser
import java.awt.BorderLayout
import java.io.File
import javax.swing.JPanel

@Composable
actual fun WebRtcPlayer(url: String, modifier: Modifier) {
    SwingPanel(
        modifier = modifier,
        factory = {
            JPanel(BorderLayout()).apply panel@{
                CefBrowserProvider.getBrowser(url)
                    .uiComponent
                    ?.let { cefComponent ->
                        add(cefComponent, BorderLayout.CENTER)
                    }
            }
        }
    )
}

object CefBrowserProvider {
    private val app: CefApp = CefAppBuilder().apply {
        setInstallDir(File("jcef-bundle"))
        addJcefArgs("--disable-web-security")
    }.build()

    private val client: CefClient = app.createClient()

    val browser: CefBrowser? = null

    fun getBrowser(httpStream: String): CefBrowser = browser ?: run {
        client.createBrowser(httpStream, false, false)
    }
}