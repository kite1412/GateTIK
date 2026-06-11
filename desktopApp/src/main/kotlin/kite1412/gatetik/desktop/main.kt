package kite1412.gatetik.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kite1412.gatetik.BuildConfig
import kite1412.gatetik.CefBrowserLoadProgress
import kite1412.gatetik.CefBrowserProgressStatus
import kite1412.gatetik.CefBrowserProvider
import kite1412.gatetik.JvmCsvExporter
import kite1412.gatetik.LocalCsvExporter
import kite1412.gatetik.app.GateTikApp
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.extension.linkStyle
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Black
import kite1412.gatetik.designsystem.theme.Blue500Alt
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Red600_90
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.di.initKoin
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.model.UserRole
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() {
    val koinApp = initKoin()
    val authentication = koinApp.koin.get<Authentication>()
    val dataStore = koinApp.koin.get<GateTikDataStore>()

    application {
        val sessionStatus by authentication
            .sessionStatus
            .collectAsState(SessionStatus.Loading)
        val darkMode by dataStore
            .observeDarkMode()
            .collectAsState(null)
        val cefLoadProgress by CefBrowserProvider
            .loadProgress
            .collectAsState()
        val isDarkMode = darkMode ?: isSystemInDarkTheme()
        val coroutineScope = rememberCoroutineScope()
        val state = rememberWindowState()

        Window(
            onCloseRequest = {
                koinApp.close()
                exitApplication()
            },
            state = state,
            title = "Gate TIK"
        ) {
            window.minimumSize = Dimension(800, 600)

            LaunchedEffect(Unit) {
                CefBrowserProvider.initApp()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyUp) {
                            if (event.isCtrlPressed) {
                                when (event.key) {
                                    Key.F -> {
                                        state.placement =
                                            if (state.placement == WindowPlacement.Maximized)
                                                WindowPlacement.Floating
                                            else
                                                WindowPlacement.Maximized

                                        true
                                    }
                                    Key.C -> {
                                        window.setLocationRelativeTo(null)

                                        true
                                    }
                                    else -> false
                                }
                            } else false
                        } else false
                    },
                contentAlignment = Alignment.Center
            ) {
                when (sessionStatus) {
                    is SessionStatus.Loading -> CircularProgressIndicator()
                    else -> {
                        GateTikTheme(darkMode = isDarkMode) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (
                                    sessionStatus is SessionStatus.SignedIn &&
                                    (sessionStatus as SessionStatus.SignedIn).user.role == UserRole.STUDENT
                                ) StudentWarning(
                                    isDarkMode = isDarkMode,
                                    onLogout = {
                                        coroutineScope.launch {
                                            authentication.logout()
                                        }
                                    }
                                ) else CompositionLocalProvider(
                                    LocalCsvExporter provides JvmCsvExporter(window)
                                ) {
                                    if (
                                        sessionStatus is SessionStatus.SignedIn &&
                                        (sessionStatus as SessionStatus.SignedIn).user.role != UserRole.STUDENT &&
                                        cefLoadProgress.status != CefBrowserProgressStatus.INITIALIZED
                                    ) CefBrowserLoadProgress(
                                        progress = cefLoadProgress,
                                        isDarkMode = isDarkMode
                                    ) else GateTikApp()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CefBrowserLoadProgress(
    progress: CefBrowserLoadProgress,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .radialBackground(isDarkMode = isDarkMode),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = Blue500Alt
        )
        Text(
            text = when (progress.status) {
                CefBrowserProgressStatus.LOCATING -> "Memeriksa komponen browser..."
                CefBrowserProgressStatus.DOWNLOADING -> "Mengunduh komponen browser (${progress.percent}%)"
                CefBrowserProgressStatus.INSTALL -> "Memasang komponen browser..."
                CefBrowserProgressStatus.INITIALIZING -> "Menjalankan browser..."
                CefBrowserProgressStatus.INITIALIZED -> "Komponen browser berhasil dipasang"
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun StudentWarning(
    isDarkMode: Boolean,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalContentColor provides if (isDarkMode) White else Black
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .radialBackground(isDarkMode)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassBox(
                modifier = Modifier.align(Alignment.TopStart),
                contentPadding = PaddingValues(0.dp),
                isDarkMode = isDarkMode
            ) {
                Text(
                    text = "KELUAR",
                    modifier = Modifier
                        .clickable(onClick = onLogout)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Red500
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(GateTikIcons.userX),
                    contentDescription = "role tidak disupport",
                    modifier = Modifier.size(80.dp),
                    tint = Red600_90
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mahasiswa tidak dapat menggunakan platform desktop",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Gunakan aplikasi Android untuk dapat mengakses gate, ")
                            withLink(
                                link = LinkAnnotation.Url(BuildConfig.ANDROID_INSTALLATION_URL)
                            ) {
                                linkStyle(isDarkMode) {
                                    append("Install Apk")
                                }
                            }
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}