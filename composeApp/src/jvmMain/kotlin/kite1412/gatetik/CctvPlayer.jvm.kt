package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.SupportedOS
import kite1412.gatetik.util.getSupportedOS
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import java.awt.BorderLayout
import java.io.File
import javax.swing.JPanel
import javax.swing.SwingUtilities

@Composable
actual fun CctvPlayer(
    modifier: Modifier,
    state: (LoadState<Unit>) -> Unit
) {
    if (isVlcInstalled()) {
        val mediaPlayerComponent = remember {
            CallbackMediaPlayerComponent()
        }

        SwingPanel(
            factory = {
                JPanel(BorderLayout()).apply {
                    add(mediaPlayerComponent, BorderLayout.CENTER)

                    SwingUtilities.invokeLater {
                        mediaPlayerComponent.mediaPlayer().media().play(
                            /*mrl = */BuildConfig.CCTV_URL,
                            /* ...options = */":rtsp-tcp"
                        )
                    }
                }
            },
            modifier = modifier
        )

        DisposableEffect(Unit) {
            onDispose {
                mediaPlayerComponent.release()
            }
        }
    }
}

fun isVlcInstalled(): Boolean = when (getSupportedOS()) {
    SupportedOS.WINDOWS -> checkWindows()
    SupportedOS.MACOS -> checkMac()
    SupportedOS.LINUX -> checkLinux()
}

private fun checkWindows(): Boolean {
    val programFiles = System.getenv("ProgramFiles") ?: "C:\\Program Files"
    val programFilesX86 = System.getenv("ProgramFiles(x86)") ?: "C:\\Program Files (x86)"

    val defaultPath = File("$programFiles\\VideoLAN\\VLC\\vlc.exe")
    val x86Path = File("$programFilesX86\\VideoLAN\\VLC\\vlc.exe")

    if (defaultPath.exists() || x86Path.exists()) return true

    // Fallback: Check registry via command line
    return try {
        val process = ProcessBuilder("reg", "query", "HKLM\\Software\\VideoLAN\\VLC").start()
        process.waitFor() == 0
    } catch (e: Exception) {
        logError(e)
        false
    }
}

private fun checkMac(): Boolean {
    val standardPath = File("/Applications/VLC.app")
    if (standardPath.exists()) return true

    // Fallback: Use system profiler to find it anywhere
    return try {
        val process = ProcessBuilder("mdfind", "kMDItemCFBundleIdentifier == 'org.videolan.vlc'").start()
        process.inputStream.bufferedReader().use { reader ->
            reader.readLine() != null
        }
    } catch (e: Exception) {
        logError(e)
        false
    }
}

private fun checkLinux(): Boolean = try {
    val process = ProcessBuilder("which", "vlc").start()
    process.waitFor() == 0
} catch (e: Exception) {
    logError(e)
    false
}

private fun logError(e: Exception) {
    Logger.e(
        tag = "jvmMain-VLC",
        message = "VLC is not installed",
        throwable = e
    )
}