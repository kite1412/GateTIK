package kite1412.gatetik.feature.monitoring.desktop.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Purple400
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Yellow500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.designsystem.util.WindowWidthSize
import kite1412.gatetik.designsystem.util.rememberWindowWidthSize
import kite1412.gatetik.feature.monitoring.desktop.ui.component.AccessLogTrend
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DashboardSummaryCard
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.ui.component.LiveCameraSection
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotificationManager
import kite1412.gatetik.feature.monitoring.desktop.ui.util.desktopBaseModifier
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.Gate
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.ui.component.GateControlButton
import kite1412.gatetik.ui.component.ParkingQuotaCard
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.ui.util.map
import kite1412.gatetik.util.timestampString
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DesktopDashboardScreen(
    contentPadding: PaddingValues,
    navigateToCctv: () -> Unit,
    navigateToAccessLogs: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DesktopDashboardViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val gate by viewModel.gate.collectAsStateWithLifecycle()
    val parkingQuota by viewModel.parkingQuota.collectAsStateWithLifecycle()
    val cctv by viewModel.cctv.collectAsStateWithLifecycle()
    val totalUsers = viewModel.totalUsers
    val accessLogs = viewModel.accessLogs

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        DesktopDashboardScreen(
            userRole = user.role,
            sideNotificationManager = viewModel.sideNotificationManager,
            gate = gate,
            parkingQuota = parkingQuota,
            cctv = cctv,
            totalUsers = totalUsers,
            accessLogs = accessLogs,
            contentPadding = contentPadding,
            onThemeToggle = viewModel::updateDarkMode,
            onOpenGate = viewModel::openGate,
            onCloseGate = viewModel::closeGate,
            onCctvFullScreenClick = navigateToCctv,
            onSeeAllAccessLogClick = navigateToAccessLogs,
            onRefreshClick = viewModel::refreshData,
            modifier = modifier
        )
    }
}

@Composable
private fun DesktopDashboardScreen(
    userRole: UserRole,
    sideNotificationManager: SideNotificationManager,
    gate: LoadState<Gate?>,
    parkingQuota: LoadState<ParkingQuota?>,
    cctv: LoadState<Cctv?>,
    totalUsers: LoadState<Int>,
    accessLogs: LoadState<List<AccessLog>>,
    contentPadding: PaddingValues,
    onThemeToggle: (darkMode: Boolean) -> Unit,
    onOpenGate: () -> Unit,
    onCloseGate: () -> Unit,
    onCctvFullScreenClick: () -> Unit,
    onSeeAllAccessLogClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowWidthSize = rememberWindowWidthSize()
    val isLargeWindow = windowWidthSize == WindowWidthSize.LARGE

    DesktopLayout(
        title = "Dashboard",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier(),
        onRefreshClick = onRefreshClick,
        sideNotificationManager = sideNotificationManager
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SummaryCardsRow(
                    gate = gate,
                    parkingQuota = parkingQuota,
                    totalUsers = totalUsers,
                    accessLogs = accessLogs
                )
            }
            if (!isLargeWindow) item {
                LiveCameraSection(
                    cameraName = cctv.data?.cameraName ?: "~",
                    showFullScreenButton = true,
                    onFullScreenClick = onCctvFullScreenClick
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isLargeWindow) LiveCameraSection(
                        cameraName = cctv.data?.cameraName ?: "~",
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        showFullScreenButton = true,
                        onFullScreenClick = onCctvFullScreenClick
                    )
                    if (isLargeWindow) Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        GateControlCard(
                            gate = gate,
                            accessLogs = accessLogs,
                            onOpenGate = onOpenGate,
                            onCloseGate = onCloseGate,
                            modifier = Modifier.weight(1f)
                        )
                        ParkingOccupancyCardSection(
                            parkingQuota = parkingQuota,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (!isLargeWindow) GateControlCard(
                        gate = gate,
                        accessLogs = accessLogs,
                        onOpenGate = onOpenGate,
                        onCloseGate = onCloseGate,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    if (!isLargeWindow) ParkingOccupancyCardSection(
                        parkingQuota = parkingQuota,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
            item {
                AccessLogTrend(
                    accessLogs = accessLogs
                )
            }
            item {
                RecentAccessActivitySection(
                    accessLogs = accessLogs,
                    onSeeAllAccessLogClick = onSeeAllAccessLogClick
                )
            }
        }
    }
}

@Composable
private fun SummaryCardsRow(
    gate: LoadState<Gate?>,
    parkingQuota: LoadState<ParkingQuota?>,
    totalUsers: LoadState<Int>,
    accessLogs: LoadState<List<AccessLog>>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DashboardSummaryCard(
            icon = painterResource(GateTikIcons.people),
            iconContainerColor = Blue500.copy(alpha = 0.1f),
            iconTint = Blue500,
            value = totalUsers.map { it.toString() },
            label = "Pengguna Aktif",
            topRightText = "Total ${totalUsers.data ?: "~"}"
        )
        DashboardSummaryCard(
            icon = painterResource(GateTikIcons.doorOpen),
            iconContainerColor = Emerald500.copy(alpha = 0.1f),
            iconTint = Emerald500,
            value = accessLogs.map { it.size.toString() },
            label = "Akses Gate\n24 Jam Terakhir",
            topRightText = "${
                accessLogs.data?.let { logs ->
                    if (logs.isEmpty()) "~" 
                    else (logs.count { it.status == AccessStatus.SUCCESS } * 100f / logs.size).roundToInt()
                } ?: "~"
            }% sukses"
        )
        DashboardSummaryCard(
            icon = painterResource(GateTikIcons.car),
            iconContainerColor = Yellow500.copy(alpha = 0.1f),
            iconTint = Yellow500,
            value = parkingQuota.map {
                if (it == null) "~"
                else "${it.usedSlots}/${it.totalSlots}"
            },
            label = "Parkir (Mahasiswa)",
            topRightText = "${
                parkingQuota.data?.let { parkingQuota ->  
                    if (parkingQuota.usedSlots == 0 || parkingQuota.totalSlots == 0) "0"
                    else "${(parkingQuota.usedSlots * 100f / parkingQuota.totalSlots).roundToInt()}"
                } ?: "~"
            }%"
        )
        DashboardSummaryCard(
            icon = painterResource(GateTikIcons.cpu),
            iconContainerColor = Purple400.copy(alpha = 0.1f),
            iconTint = Purple400,
            value = gate.map {
                it?.iotDevice?.status
                    ?.name
                    ?.uppercase()
                    ?.replaceFirstChar { c -> c.uppercase() } ?: "Offline"
            },
            label = "Perangkat IoT",
            topRightText = gate.data?.iotDevice?.deviceName ?: "~"
        )
        DashboardSummaryCard(
            icon = painterResource(GateTikIcons.phoneCall),
            iconContainerColor = Red500.copy(alpha = 0.1f),
            iconTint = Red500,
            value = LoadState.Success("Aktif"),
            label = "Panggilan Pengunjung",
            topRightText = "Sedang berlangsung"
        )
    }
}

@Composable
private fun GateControlCard(
    gate: LoadState<Gate?>,
    accessLogs: LoadState<List<AccessLog>>,
    onOpenGate: () -> Unit,
    onCloseGate: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBox(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = gate.data?.gateName ?: "~",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(GateTikIcons.wifi),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Emerald500
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${
                                gate.data
                                    ?.iotDevice
                                    ?.status
                                    ?.capitalizedName
                            } • Terakhir dibuka ${
                                accessLogs.data
                                    ?.takeIf { it.isNotEmpty() }
                                    ?.maxOf { it.createdAt }
                                    ?.timestampString
                                    ?: "~"
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Operator: Super Admin",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Yellow500, CircleShape)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val textStyle = MaterialTheme.typography.labelSmall

                GateControlButton(
                    isOpen = true,
                    actionEnabled = true,
                    onClick = onOpenGate,
                    backgroundBrush = SolidColor(Blue500),
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textStyle = textStyle
                )
                GateControlButton(
                    isOpen = false,
                    actionEnabled = true,
                    onClick = onCloseGate,
                    backgroundBrush = SolidColor(Red500),
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textStyle = textStyle
                )
            }
        }
    }
}

@Composable
private fun ParkingOccupancyCardSection(
    parkingQuota: LoadState<ParkingQuota?>,
    modifier: Modifier = Modifier
) {
    GlassBox(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OKUPANSI PARKIR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Badge(
                    text = "Tersedia",
                    containerColor = Emerald500.copy(alpha = 0.1f),
                    contentColor = Emerald500
                )
            }

            ParkingQuotaCard(
                parkingQuota = parkingQuota,
                isDarkMode = LocalDarkMode.current
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = parkingQuota.data?.totalSlots?.toString() ?: "~",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Terpakai",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = parkingQuota.data?.usedSlots?.toString() ?: "~",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tersedia",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = parkingQuota.data?.run {
                            totalSlots - usedSlots
                        }?.toString() ?: "~",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "Lihat Detail ↗",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Blue500
                )
            }
        }
    }
}

@Composable
private fun RecentAccessActivitySection(
    accessLogs: LoadState<List<AccessLog>>,
    onSeeAllAccessLogClick: () -> Unit,
) {
    GlassBox {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aktivitas Akses Terbaru",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Lihat semua",
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = onSeeAllAccessLogClick
                    ),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Blue500
                )
            }
            LoadState(
                state = accessLogs,
                loading = { CircularProgressIndicator() },
                error = { Text("Gagal memuat log akses") },
                success = { accessLogs ->
                    if (accessLogs.isNotEmpty()) CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodySmall
                    ) {
                        Table(
                            columns = listOf(
                                TableColumn("PENGGUNA", 2f) {
                                    Text(
                                        text = it.userFullName,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                TableColumn("ROLE") {
                                    Text(it.userRole.toIdString())
                                },
                                TableColumn("AKSI") {
                                    Text(it.action.capitalizedName)
                                },
                                TableColumn("METODE") {
                                    Text(it.accessMethod.capitalizedName)
                                },
                                TableColumn("STATUS") {
                                    val (containerColor, contentColor) = when (it.status) {
                                        AccessStatus.SUCCESS -> Emerald500.copy(alpha = 0.1f) to Emerald500
                                        AccessStatus.FAILED -> Red500.copy(alpha = 0.1f) to Red500
                                        AccessStatus.PENDING -> Yellow500.copy(alpha = 0.1f) to Yellow500
                                    }
                                    Badge(
                                        text = it.status.capitalizedName,
                                        containerColor = containerColor,
                                        contentColor = contentColor
                                    )
                                },
                                TableColumn("CATATAN", 2f) {
                                    Text(it.notes ?: "")
                                },
                                TableColumn("WAKTU", 2f) {
                                    Text(it.createdAt.timestampString)
                                }
                            ),
                            items = accessLogs
                        )
                    } else Text(
                        text = "Tidak ada akses log",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            )

            if (accessLogs.data == null) Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada data log akses.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopDashboardScreenPreview() {
    GateTikTheme {
        Scaffold { p ->
            DesktopDashboardScreen(
                contentPadding = PaddingValues(24.dp),
                navigateToCctv = {},
                navigateToAccessLogs = {},
                modifier = Modifier.padding(p)
            )
        }
    }
}
