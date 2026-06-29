package kite1412.gatetik.feature.monitoring.mobile.intercom

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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import kite1412.gatetik.MicLevelMonitor
import kite1412.gatetik.WebRtcPlayer
import kite1412.gatetik.designsystem.component.AudioLevelMeter
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
import kite1412.gatetik.network.mock.mockCctvs
import kite1412.gatetik.rememberRecordAudioPermissionRequester
import kite1412.gatetik.ui.component.InfoCard
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.UiLoadState
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.ui.util.navBarPadding
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun MobileIntercomScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileIntercomViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val intercomCameras by viewModel.intercomCameras.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar) snackbarHostStateWrapper
                .showSnackbar(event.message)
        }
    }

    MobileIntercomScreen(
        intercomCameras = intercomCameras,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::onRefresh,
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileIntercomScreen(
    intercomCameras: LoadState<List<Cctv>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    var fullScreenCctv by retain { mutableStateOf<Cctv?>(null) }
    var micOnCctvIds by rememberSaveable { mutableStateOf(emptySet<Int>()) }

    var pendingMicOnCctv by retain { mutableStateOf<Cctv?>(null) }
    val recordAudioPermissionRequester = rememberRecordAudioPermissionRequester { granted ->
        if (granted) {
            pendingMicOnCctv?.let { cctv ->
                micOnCctvIds = micOnCctvIds + cctv.id
                fullScreenCctv = cctv
            }
        }
        pendingMicOnCctv = null
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            UiLoadState(
                state = intercomCameras,
                modifier = Modifier.fillMaxSize(),
                onRetry = onRefresh
            ) {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(contentPadding),
                    contentPadding = navBarPadding(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        SectionHeader(
                            title = "Interkom",
                            subtitle = "Komunikasi dua arah area gate"
                        )
                    }
                    items(intercomCameras.data ?: emptyList()) { cctv ->
                        val isMicOn = micOnCctvIds.contains(cctv.id)
                        CctvPlayer(
                            cctv = cctv,
                            isDarkMode = isDarkMode,
                            isMicOn = isMicOn,
                            onMicOnChange = { isOn ->
                                if (isOn) {
                                    pendingMicOnCctv = cctv
                                    recordAudioPermissionRequester()
                                } else {
                                    micOnCctvIds = micOnCctvIds - cctv.id
                                }
                            },
                            onFullScreenClick = { fullScreenCctv = cctv }
                        )
                    }
                    item {
                        InfoCard(
                            icon = painterResource(GateTikIcons.phone),
                            title = "Interkom Gate",
                            description = "Gunakan fitur ini untuk berkomunikasi dengan pengunjung di area gate."
                        )
                    }
                }
            }
            fullScreenCctv?.let { cctv ->
                val onDismissRequest = {
                    fullScreenCctv = null
                    micOnCctvIds = micOnCctvIds - cctv.id
                }

                Dialog(
                    onDismissRequest = {
                        onDismissRequest()
                        MicLevelMonitor.stop()
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
                            onClick = onDismissRequest,
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
                            val url = getWebRtcStreamUrl(cctv.path) + if (isMicOn) "&media=video+audio+microphone" else ""
                            key(url) {
                                WebRtcPlayer(
                                    url = url,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        if (cctv.type == CctvType.INTERCOM) {
                            val micLevel by MicLevelMonitor.level.collectAsStateWithLifecycle()

                            LaunchedEffect(isMicOn) {
                                if (isMicOn) MicLevelMonitor.start() else MicLevelMonitor.stop()
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isMicOn) AudioLevelMeter(level = micLevel) else Box(Modifier.size(1.dp))

                                IntercomMic(
                                    isMicOn = isMicOn,
                                    onMicOnChange = { isOn ->
                                        if (isOn) {
                                            pendingMicOnCctv = cctv
                                            recordAudioPermissionRequester()
                                        } else {
                                            micOnCctvIds = micOnCctvIds - cctv.id
                                            MicLevelMonitor.stop()
                                        }
                                    }
                                )
                            }
                        }
                    }
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
                val url = getWebRtcStreamUrl(cctv.path)
                key(url) {
                    WebRtcPlayer(
                        url = url,
                        modifier = Modifier.fillMaxSize()
                    )
                }
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
private fun MobileIntercomScreenPreview() {
    GateTikTheme {
        Scaffold { p ->
            MobileIntercomScreen(
                intercomCameras = LoadState.Success(mockCctvs.filter { it.type == CctvType.INTERCOM }),
                isRefreshing = false,
                onRefresh = {},
                contentPadding = p
            )
        }
    }
}
