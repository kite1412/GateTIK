package kite1412.gatetik.feature.monitoring.mobile.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.WebRtcPlayer
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.theme.BlueIndigoGradient
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray900
import kite1412.gatetik.designsystem.theme.Gray950
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Red600
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White20
import kite1412.gatetik.designsystem.theme.White60
import kite1412.gatetik.designsystem.theme.White70
import kite1412.gatetik.designsystem.theme.White80
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.getWebRtcStreamUrl
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.model.Gate
import kite1412.gatetik.model.IotDeviceStatus
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.rememberRecordAudioPermissionRequester
import kite1412.gatetik.ui.component.ActionCard
import kite1412.gatetik.ui.component.GateControlButton
import kite1412.gatetik.ui.component.HeaderSection
import kite1412.gatetik.ui.component.ParkingQuotaCard
import kite1412.gatetik.ui.component.SmallCircularProgressIndicator
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.navBarPadding
import kite1412.gatetik.util.timeAgo
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun MobileHomeScreen(
    contentPadding: PaddingValues,
    navigateToParking: () -> Unit,
    navigateToCctv: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MobileHomeViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val signedInUser by viewModel.signedInUser.collectAsStateWithLifecycle()
    val mainGate by viewModel.mainGate.collectAsStateWithLifecycle()
    val cctvs by viewModel.cctvs.collectAsStateWithLifecycle()
    val mainParkingQuota by viewModel.mainParkingQuota.collectAsStateWithLifecycle()
    val latestAccessLog by viewModel.latestAccessLog.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar) snackbarHostStateWrapper
                .showSnackbar(event.message)
        }
    }
    signedInUser?.let { user ->
        MobileHomeScreen(
            userName = user.fullName,
            userRole = user.role,
            gate = mainGate,
            latestAccessLog = latestAccessLog,
            parkingQuota = mainParkingQuota,
            cctvs = cctvs,
            isRefreshing = viewModel.isRefreshing,
            onRefresh = viewModel::onRefresh,
            contentPadding = contentPadding,
            onOpenGate = viewModel::openGate,
            onCloseGate = viewModel::closeGate,
            onParkingClick = navigateToParking,
            onCctvClick = navigateToCctv,
            modifier = modifier
        )
    }
}

@Composable
private fun MobileHomeScreen(
    userName: String,
    userRole: UserRole,
    gate: LoadState<Gate?>,
    latestAccessLog: AccessLog?,
    parkingQuota: LoadState<ParkingQuota?>,
    cctvs: LoadState<List<Cctv>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    contentPadding: PaddingValues,
    onOpenGate: () -> Unit,
    onCloseGate: () -> Unit,
    onParkingClick: () -> Unit,
    onCctvClick: () -> Unit,
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentPadding = navBarPadding(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    HeaderSection(
                        userName = userName,
                        userRole = userRole,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.padding(start = 8.dp),
                        trailing = {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = if (isDarkMode) White20 else MaterialTheme.colorScheme.surface,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(GateTikIcons.bell),
                                    contentDescription = "Notifikasi",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    )
                }
                item {
                    GateControlCard(
                        gate = gate,
                        latestAccessLog = latestAccessLog,
                        onOpenGate = onOpenGate,
                        onCloseGate = onCloseGate
                    )
                }
//        item {
//            GateAccessButton(
//                isLocked = true,
//                onLockChange = {},
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
                item {
                    CctvCard(cctvs = cctvs) { cctv ->
                        pendingMicOnCctv = cctv
                        recordAudioPermissionRequester()
                    }
                }
                item {
                    ParkingQuotaCard(
                        parkingQuota = parkingQuota,
                        isDarkMode = isDarkMode
                    )
                }
                item {
                    QuickActionsRow(
                        onParkingClick = onParkingClick,
                        onCctvClick = onCctvClick
                    )
                }
            }

            fullScreenCctv?.let { cctv ->
                val onDismissRequest = {
                    fullScreenCctv = null
                    micOnCctvIds = micOnCctvIds - cctv.id
                }

                Dialog(
                    onDismissRequest = onDismissRequest,
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
                                contentDescription = null,
                                tint = White
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
                        if (cctv.type == CctvType.INTERCOM) IntercomMic(
                            isMicOn = isMicOn,
                            onMicOnChange = { isOn ->
                                if (isOn) {
                                    pendingMicOnCctv = cctv
                                    recordAudioPermissionRequester()
                                } else {
                                    micOnCctvIds = micOnCctvIds - cctv.id
                                }
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
}

@Composable
private fun GateAccessButton(
    isLocked: Boolean,
    onLockChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val text = "${if (isLocked) "Buka " else  ""}Kunci Gate"
    val color by animateColorAsState(
        targetValue = if (isLocked) Emerald500 else Red500.copy(alpha = 0.7f)
    )
    val background by animateColorAsState(
        targetValue = if (isLocked) Emerald500.copy(alpha = 0.1f) else Red500.copy(alpha = 0.1f)
    )

    Button(
        onClick = { onLockChange(!isLocked) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = background
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    if (isLocked) GateTikIcons.lockOpen else GateTikIcons.lock
                ),
                contentDescription = text,
                tint = color
            )
            Text(
                text = text,
                color = color
            )
        }
    }
}

@Composable
private fun GateControlCard(
    gate: LoadState<Gate?>,
    latestAccessLog: AccessLog?,
    onOpenGate: () -> Unit,
    onCloseGate: () -> Unit,
    isLocked: Boolean = false
) {
    val isGateSuccess = gate is LoadState.Success && gate.data != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Brush.linearGradient(BlueIndigoGradient))
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "GATE UTAMA",
                    style = MaterialTheme.typography.labelMedium,
                    color = White80,
                    letterSpacing = 1.sp
                )
                AnimatedVisibility(
                    visible = isGateSuccess,
                    enter = fadeIn() + slideInHorizontally { -it }
                ) {
                    (if (isGateSuccess) gate.data else null)?.let { gate ->
                        Text(
                            text = gate.gateName,
                            style = MaterialTheme.typography.labelSmall,
                            color = White60
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val color = White.copy(alpha = 0.6f)
                    val isDeviceOnline = if (gate is LoadState.Success)
                        gate.data?.iotDevice?.status == IotDeviceStatus.ONLINE else false
                    val contentColor = if (isDeviceOnline) Emerald500 else color

                    Icon(
                        painter = painterResource(GateTikIcons.wifi),
                        contentDescription = null,
                        tint = contentColor
                    )
                    LoadState(
                        state = gate,
                        loading = {
                            SmallCircularProgressIndicator(color = color)
                        },
                        error = {
                            Text(
                                text = "IoT device tidak ditemukan",
                                style = MaterialTheme.typography.bodySmall,
                                color = Red500
                            )
                        },
                        success = {
                            Text(
                                text = "Perangkat " + if (isDeviceOnline) "terhubung" else "terputus",
                                color = contentColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )
                }

                latestAccessLog?.let { latestLog ->
                    Text(
                        text = "Terakhir diakses: ${latestLog.updatedAt.timeAgo()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = White60,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GateControlButton(
                    isOpen = true,
                    actionEnabled = true,
                    onClick = onOpenGate
                )
                GateControlButton(
                    isOpen = false,
                    actionEnabled = true,
                    onClick = onCloseGate
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(64.dp)
                .background(
                    color = White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            val animatedColor by animateColorAsState(
                targetValue = if (isLocked) Red500.copy(alpha = 0.7f) else White
            )

            Icon(
                painter = painterResource(
                    if (isLocked) GateTikIcons.lock else GateTikIcons.lockOpen
                ),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = animatedColor
            )
        }
    }
}

@Composable
private fun CctvCard(
    cctvs: LoadState<List<Cctv>>,
    onMicClick: (Cctv) -> Unit
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Gray900, Color.Black)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 0f
                        )
                    )
            )

            LoadState(
                state = cctvs,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Gray950.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        SmallCircularProgressIndicator(
                            color = White
                        )
                    }
                },
                error = {
                    Text(
                        text = "Cctv tidak ditemukan",
                        modifier = Modifier.align(Alignment.Center),
                        color = White
                    )
                },
                success = { list ->
                    val cctv = list.getOrNull(currentIndex)
                    if (cctv != null) {
                        var showMessage by rememberSaveable { mutableStateOf(true) }

                        LaunchedEffect(cctv.id) {
                            showMessage = true
                            delay(3.seconds)
                            showMessage = false
                        }
                        val url = getWebRtcStreamUrl(cctv.path)
                        key(url) {
                            WebRtcPlayer(
                                url = url,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
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
                        Badge(
                            text = "LIVE",
                            containerColor = Red600,
                            contentColor = White,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.TopEnd)
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(GateTikIcons.videoRecorder),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = White
                            )
                            Text(
                                text = cctv.cameraName,
                                style = MaterialTheme.typography.bodySmall,
                                color = White
                            )
                        }

                        if (list.size > 1) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = {
                                        currentIndex = (currentIndex - 1 + list.size) % list.size
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(GateTikIcons.chevronRight),
                                        contentDescription = "Previous",
                                        modifier = Modifier.rotate(180f),
                                        tint = White
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        currentIndex = (currentIndex + 1) % list.size
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(GateTikIcons.chevronRight),
                                        contentDescription = "Next",
                                        tint = White
                                    )
                                }
                            }
                        }

                        if (cctv.type == CctvType.INTERCOM) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                                    .clip(CircleShape)
                                    .background(White20)
                                    .clickable { onMicClick(cctv) }
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(GateTikIcons.mic),
                                    contentDescription = "Intercom",
                                    modifier = Modifier.size(20.dp),
                                    tint = White
                                )
                            }
                        }
                    } else Text(
                        text = "Cctv tidak ditemukan",
                        modifier = Modifier.align(Alignment.Center),
                        color = White
                    )
                }
            )
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

@Composable
private fun QuickActionsRow(
    onParkingClick: () -> Unit,
    onCctvClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionCard(
            icon = painterResource(GateTikIcons.car),
            label = "Parkir",
            onClick = onParkingClick,
            modifier = Modifier.weight(1f)
        )
        ActionCard(
            icon = painterResource(GateTikIcons.videoRecorder),
            label = "Cctv",
            onClick = onCctvClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@DevicePreviews
@Composable
private fun MobileHomeScreenPreview() {
    GateTikTheme(darkMode = isSystemInDarkTheme()) {
        Scaffold { p ->
            MobileHomeScreen(
                userName = "Aulia Rahman",
                userRole = UserRole.ADMIN,
                gate = LoadState.Loading(),
                latestAccessLog = null,
                parkingQuota = LoadState.Loading(),
                cctvs = LoadState.Loading(),
                isRefreshing = false,
                onRefresh = {},
                contentPadding = p,
                onOpenGate = {},
                onCloseGate = {},
                onParkingClick = {},
                onCctvClick = {}
            )
        }
    }
}
