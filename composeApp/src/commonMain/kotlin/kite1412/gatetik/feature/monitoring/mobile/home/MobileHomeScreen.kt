package kite1412.gatetik.feature.monitoring.mobile.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White20
import kite1412.gatetik.designsystem.theme.White60
import kite1412.gatetik.designsystem.theme.White80
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.getWebRtcStreamUrl
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.Gate
import kite1412.gatetik.model.IotDeviceStatus
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.model.UserRole
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
    val mainCctv by viewModel.mainCctv.collectAsStateWithLifecycle()
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
            cctv = mainCctv,
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
    cctv: LoadState<Cctv?>,
    contentPadding: PaddingValues,
    onOpenGate: () -> Unit,
    onCloseGate: () -> Unit,
    onParkingClick: () -> Unit,
    onCctvClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current

    LazyColumn(
        modifier = modifier
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
        item { CctvCard(cctv = cctv) }
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
    cctv: LoadState<Cctv?>
) {
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
                state = cctv,
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
                success = { cctv ->
                    if (cctv != null) {
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
                            containerColor = Red500,
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
                cctv = LoadState.Loading(),
                contentPadding = p,
                onOpenGate = {},
                onCloseGate = {},
                onParkingClick = {},
                onCctvClick = {}
            )
        }
    }
}
