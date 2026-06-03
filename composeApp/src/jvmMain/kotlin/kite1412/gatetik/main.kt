package kite1412.gatetik

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kite1412.gatetik.app.GateTikApp
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.extension.linkStyle
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Black
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Red600_90
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.Yellow500
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
        val isDarkMode = darkMode ?: isSystemInDarkTheme()
        var showVlcWarning by retain {
            mutableStateOf(true)
        }
        val coroutineScope = rememberCoroutineScope()

        Window(
            onCloseRequest = {
                koinApp.close()
                exitApplication()
            },
            title = "Gate TIK"
        ) {
            window.minimumSize = Dimension(800, 600)

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (sessionStatus) {
                    is SessionStatus.Loading -> CircularProgressIndicator()
                    else -> {
                        GateTikTheme(darkMode = isDarkMode) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AnimatedVisibility(
                                    sessionStatus is SessionStatus.SignedIn &&
                                            (sessionStatus as SessionStatus.SignedIn).user.role != UserRole.STUDENT &&
                                            !isVlcInstalled() &&
                                            showVlcWarning
                                ) {
                                    VlcNotInstalledWarning(
                                        onDismissClick = { showVlcWarning = false }
                                    )
                                }
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
                                ) else GateTikApp()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VlcNotInstalledWarning(
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = Yellow500
    val labelSmall = MaterialTheme.typography.labelSmall

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
            .background(color.copy(alpha = 0.1f))
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = buildAnnotatedString { 
                append("VLC Media Player belum terinstall, install agar dapat menggunakan fitur CCTV monitoring, ")
                withLink(
                    link = LinkAnnotation.Url(
                        url = "https://images.videolan.org/vlc/"
                    )
                ) {
                    linkStyle(false) {
                        append("Install")
                    }
                }
            },
            color = color,
            style = labelSmall
        )
        Icon(
            painter = painterResource(GateTikIcons.x),
            contentDescription = "dismiss",
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .size((labelSmall.fontSize.value * 1.7f).dp)
                .clickable(onClick = onDismissClick),
            tint = color
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