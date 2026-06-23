package kite1412.gatetik.feature.monitoring.mobile.cctv

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.WebRtcPlayer
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.SectionHeader
import kite1412.gatetik.designsystem.theme.Black
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red600
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White20
import kite1412.gatetik.designsystem.theme.White70
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.getWebRtcStreamUrl
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.ui.component.InfoCard
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiLoadState
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.ui.util.navBarPadding
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun MobileCctvScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileCctvViewModel = koinViewModel()
) {
    val cctvs by viewModel.cctvs.collectAsStateWithLifecycle()

    MobileCctvScreen(
        cctvs = cctvs,
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileCctvScreen(
    cctvs: LoadState<List<Cctv>>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    var fullScreenCctv by retain { mutableStateOf<Cctv?>(null) }
    var micOnCctvIds by rememberSaveable { mutableStateOf(emptySet<Int>()) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        UiLoadState(
            state = cctvs,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.padding(contentPadding),
                contentPadding = navBarPadding(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    SectionHeader(
                        title = "CCTV",
                        subtitle = "Sistem monitoring area gate"
                    )
                }
                items(cctvs.data ?: emptyList()) { cctv ->
                    val isMicOn = micOnCctvIds.contains(cctv.id)
                    CctvPlayer(
                        cctv = cctv,
                        isDarkMode = isDarkMode,
                        isMicOn = isMicOn,
                        onMicOnChange = { isOn ->
                            micOnCctvIds = if (isOn) micOnCctvIds + cctv.id else micOnCctvIds - cctv.id
                            if (isOn) fullScreenCctv = cctv
                        },
                        onFullScreenClick = { fullScreenCctv = cctv }
                    )
                }
                item {
                    InfoCard(
                        icon = painterResource(GateTikIcons.phone),
                        title = "CCTV Monitoring",
                        description = "Sistem monitoring untuk memantau aktivitas di area gate."
                    )
                }
            }
        }
        fullScreenCctv?.let { cctv ->
            Dialog(
                onDismissRequest = {
                    fullScreenCctv = null
                    micOnCctvIds = micOnCctvIds - cctv.id
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                val isMicOn = micOnCctvIds.contains(cctv.id)

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { fullScreenCctv = null },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.x),
                            contentDescription = null
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    ) {
                        WebRtcPlayer(
                            url = getWebRtcStreamUrl(cctv.path) + if (isMicOn) "&media=video+audio+microphone" else "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (cctv.type == CctvType.INTERCOM) IntercomMic(
                        isMicOn = isMicOn,
                        onMicOnChange = { isOn ->
                            micOnCctvIds = if (isOn) micOnCctvIds + cctv.id else micOnCctvIds - cctv.id
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CctvPlayer(
    cctv: Cctv,
    isDarkMode: Boolean,
    isMicOn: Boolean,
    onMicOnChange: (Boolean) -> Unit,
    onFullScreenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                var showMessage by rememberSaveable { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    if (showMessage) {
                        delay(3.seconds)
                        showMessage = false
                    }
                }
                WebRtcPlayer(
                    url = getWebRtcStreamUrl(cctv.path),
                    modifier = Modifier.fillMaxSize()
                )
                Badge(
                    text = "LIVE",
                    containerColor = Red600,
                    contentColor = White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
                this@Column.AnimatedVisibility(
                    visible = showMessage,
                    modifier = Modifier.align(Alignment.Center),
                    exit = fadeOut(),
                    enter = fadeIn()
                ) {
                    Text(
                        text = "Pastikan terhubung dengan Wi-Fi TIK",
                        style = MaterialTheme.typography.labelSmall,
                        color = White
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(
                    text = cctv.cameraName,
                    containerColor = if (isDarkMode) White20 else Black.copy(alpha = 0.3f),
                    contentColor = White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (cctv.type == CctvType.INTERCOM) IntercomMic(
                        isMicOn = isMicOn,
                        onMicOnChange = onMicOnChange,
                        color = if (isMicOn) Emerald500 else MaterialTheme.colorScheme.onBackground
                    )

                    Icon(
                        painter = painterResource(GateTikIcons.zoomIn),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = onFullScreenClick
                            ),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun IntercomMic(
    isMicOn: Boolean,
    onMicOnChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = if (isMicOn) Emerald500 else White70
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onMicOnChange(!isMicOn) }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (isMicOn) "Mic On" else "Mic Off",
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
        Icon(
            painter = painterResource(if (isMicOn) GateTikIcons.mic else GateTikIcons.micOff),
            contentDescription = null,
            tint = color
        )
    }
}

@DevicePreviews
@Composable
private fun MobileCctvScreenPreview() {
    GateTikTheme {
        Scaffold { p ->
              MobileCctvScreen(
                  contentPadding = p
              )
        }
    }
}
