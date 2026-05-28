package kite1412.portaltik.feature.monitoring.mobile.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.CctvPlayer
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.Blue200
import kite1412.portaltik.designsystem.theme.Blue200_50
import kite1412.portaltik.designsystem.theme.Blue900
import kite1412.portaltik.designsystem.theme.BlueIndigoGradient
import kite1412.portaltik.designsystem.theme.Emerald500
import kite1412.portaltik.designsystem.theme.Gray900
import kite1412.portaltik.designsystem.theme.Gray950
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Red500
import kite1412.portaltik.designsystem.theme.Slate400
import kite1412.portaltik.designsystem.theme.Slate500
import kite1412.portaltik.designsystem.theme.Slate900_95
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White20
import kite1412.portaltik.designsystem.theme.White55
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.designsystem.theme.White80
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.model.Cctv
import kite1412.portaltik.model.Gate
import kite1412.portaltik.model.IotDeviceStatus
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.ui.component.ActionCard
import kite1412.portaltik.ui.component.GateControlButton
import kite1412.portaltik.ui.component.SmallCircularProgressIndicator
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.UiEvent
import kite1412.portaltik.util.now
import kite1412.portaltik.util.timeAgo
import kite1412.portaltik.util.toLocalDateTime
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

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
    MobileHomeScreen(
        userName = signedInUser?.fullName ?: "",
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

@Composable
private fun MobileHomeScreen(
    userName: String,
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
    val scaffoldComponentsController = LocalScaffoldComponentsController.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(
            bottom = scaffoldComponentsController.getState(ScaffoldComponent.NAV_BAR).size.height
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            HeaderSection(
                userName = userName,
                isDarkMode = isDarkMode,
                modifier = Modifier.padding(start = 8.dp)
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
        item {
            GateAccessButton(
                isLocked = true,
                onLockChange = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
        item { CctvCard(cctv = cctv) }
        item {
            ParkingStatusCard(
                isDarkMode = isDarkMode,
                parkingQuota = parkingQuota
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
                    if (isLocked) PortalTikIcons.lockOpen else PortalTikIcons.lock
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
private fun HeaderSection(
    userName: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val currentHour = now().toLocalDateTime().hour
    val greeting = when (currentHour) {
        in 6..11 -> "Selamat pagi,"
        in 12..15 -> "Selamat siang,"
        in 16..18 -> "Selamat sore,"
        else -> "Selamat malam,"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDarkMode) Slate400 else Slate500
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Badge(
                text = "ADMIN",
                containerColor = if (isDarkMode) Blue900.copy(alpha = 0.4f) else Blue200.copy(alpha = 0.4f),
                contentColor = if (isDarkMode) Blue200 else Blue900
            )

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
                    painter = painterResource(PortalTikIcons.bell),
                    contentDescription = "Notifikasi",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun GateControlCard(
    gate: LoadState<Gate?>,
    latestAccessLog: AccessLog?,
    onOpenGate: () -> Unit,
    onCloseGate: () -> Unit,
    isLocked: Boolean = true
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
                        painter = painterResource(PortalTikIcons.wifi),
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
                    gateState = gate,
                    onClick = onOpenGate
                )
                GateControlButton(
                    isOpen = false,
                    gateState = gate,
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
                    if (isLocked) PortalTikIcons.lock else PortalTikIcons.lockOpen
                ),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = animatedColor
            )
        }
    }
}

@Composable
private fun ParkingStatusCard(
    isDarkMode: Boolean,
    parkingQuota: LoadState<ParkingQuota?>
) {
    val background = if (isDarkMode) Slate900_95 else White55
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .run {
                if (!isDarkMode) border(
                    width = 2.dp,
                    color = Blue200_50,
                    shape = shape
                ) else this
            }
            .clickable { /* Handle Parking Click */ }
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = Emerald500.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(PortalTikIcons.car),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Emerald500
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Parkir (Mahasiswa)",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDarkMode) Slate400 else Slate500
                )
                val color = MaterialTheme.colorScheme.onSurface

                LoadState(
                    state = parkingQuota,
                    loading = {
                        SmallCircularProgressIndicator(color = color)
                    },
                    error = {
                        Text(
                            text = "Tidak dapat memuat kuota parkir",
                            style = MaterialTheme.typography.bodySmall,
                            color = color
                        )
                    },
                    success = { parkingQuota ->
                        if (parkingQuota != null) Text(
                            text = "${parkingQuota.usedSlots} / ${parkingQuota.totalSlots} slot",
                            style = MaterialTheme.typography.titleSmall,
                            color = color
                        ) else Text(
                            text = "Kuota parkir belum diatur"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))

                val progress by animateFloatAsState(
                    targetValue = if (parkingQuota is LoadState.Success && parkingQuota.data != null) {
                        val data = parkingQuota.data

                        data.usedSlots / data.totalSlots.toFloat()
                    } else 0f
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Emerald500,
                    trackColor = if (isDarkMode) White.copy(alpha = 0.1f) else Blue200.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {},
                    gapSize = 2.dp
                )
            }
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
                .aspectRatio(16f / 10f)
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
                                delay(3000)
                                showMessage = false
                            }
                        }
                        CctvPlayer(Modifier.fillMaxSize()) {}
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
                                painter = painterResource(PortalTikIcons.videoRecorder),
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
            icon = painterResource(PortalTikIcons.car),
            label = "Parkir",
            onClick = onParkingClick,
            modifier = Modifier.weight(1f)
        )
        ActionCard(
            icon = painterResource(PortalTikIcons.videoRecorder),
            label = "Cctv",
            onClick = onCctvClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@DevicePreviews
@Composable
private fun MobileHomeScreenPreview() {
    PortalTikTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold { p ->
            MobileHomeScreen(
                userName = "Aulia Rahman",
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
