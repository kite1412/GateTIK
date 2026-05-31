package kite1412.portaltik.feature.monitoring.desktop.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.Emerald500
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Purple400
import kite1412.portaltik.designsystem.theme.Red500
import kite1412.portaltik.designsystem.theme.Yellow500
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.designsystem.util.WindowWidthSize
import kite1412.portaltik.designsystem.util.rememberWindowWidthSize
import kite1412.portaltik.feature.monitoring.desktop.component.DashboardSummaryCard
import kite1412.portaltik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.portaltik.feature.monitoring.desktop.component.LiveCameraSection
import kite1412.portaltik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.ui.component.GateControlButton
import kite1412.portaltik.ui.component.ParkingQuotaCard
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.LoadState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopDashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopDashboardViewModel = koinViewModel()
) {
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()

    user?.let { user ->
        DesktopDashboardScreen(
            userRole = user.role,
            contentPadding = contentPadding,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier
        )
    }
}

@Composable
private fun DesktopDashboardScreen(
    userRole: UserRole,
    contentPadding: PaddingValues,
    onThemeToggle: (darkMode: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowWidthSize = rememberWindowWidthSize()
    val isLargeWindow = windowWidthSize == WindowWidthSize.LARGE

    DesktopLayout(
        title = "Dashboard",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SummaryCardsRow()
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isLargeWindow) Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        GateControlCard(
                            modifier = Modifier.weight(1f)
                        )
                        ParkingOccupancyCardSection(
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (!isLargeWindow) GateControlCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    if (!isLargeWindow) ParkingOccupancyCardSection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    if (isLargeWindow) LiveCameraSection(
                        cameraName = "CAM-01 · Gerbang Utama",
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                    )
                }
            }
            if (!isLargeWindow) item {
                LiveCameraSection(
                    cameraName = "CAM-01 · Gerbang Utama"
                )
            }
            item {
                AccessTrendSection()
            }
            item {
                RecentAccessActivitySection()
            }
        }
    }
}

@Composable
private fun SummaryCardsRow(
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DashboardSummaryCard(
            icon = painterResource(PortalTikIcons.people),
            iconContainerColor = Blue500.copy(alpha = 0.1f),
            iconTint = Blue500,
            value = "1",
            label = "Pengguna Aktif",
            topRightText = "Total 2"
        )
        DashboardSummaryCard(
            icon = painterResource(PortalTikIcons.doorOpen),
            iconContainerColor = Emerald500.copy(alpha = 0.1f),
            iconTint = Emerald500,
            value = "0",
            label = "Pemicu Gerbang",
            topRightText = "0% sukses"
        )
        DashboardSummaryCard(
            icon = painterResource(PortalTikIcons.car),
            iconContainerColor = Yellow500.copy(alpha = 0.1f),
            iconTint = Yellow500,
            value = "0/50",
            label = "Parkir (Mahasiswa)",
            topRightText = "0%"
        )
        DashboardSummaryCard(
            icon = painterResource(PortalTikIcons.cpu),
            iconContainerColor = Purple400.copy(alpha = 0.1f),
            iconTint = Purple400,
            value = "Offline",
            label = "Perangkat IoT",
            topRightText = "ESP8266 Gerbang 1"
        )
        DashboardSummaryCard(
            icon = painterResource(PortalTikIcons.phoneCall),
            iconContainerColor = Red500.copy(alpha = 0.1f),
            iconTint = Red500,
            value = "Aktif",
            label = "Panggilan Pengunjung",
            topRightText = "Sedang berlangsung"
        )
    }
}

@Composable
private fun GateControlCard(
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
                Column {
                    Text(
                        text = "GERBANG UTAMA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(PortalTikIcons.wifi),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Emerald500
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Online • Terakhir dibuka —",
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
                GateControlButton(
                    isOpen = true,
                    actionEnabled = true,
                    onClick = {},
                    backgroundBrush = SolidColor(Blue500),
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                GateControlButton(
                    isOpen = false,
                    actionEnabled = true,
                    onClick = {},
                    backgroundBrush = SolidColor(Red500),
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ParkingOccupancyCardSection(
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
                parkingQuota = LoadState.Success(
                    ParkingQuota(
                        id = 1,
                        totalSlots = 50,
                        usedSlots = 0,
                        autoRestrictStudents = false
                    )
                ),
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
                        text = "50",
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
                        text = "0",
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
                        text = "50",
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
private fun AccessTrendSection() {
    GlassBox {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Text(
                    text = "TREN AKSES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "24 jam terakhir",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Transparent)
            ) {
                // Placeholder for chart
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    repeat(5) { i ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${4 - i}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(24.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                            )
                        }
                    }
                }
                
                // Bottom timeline labels
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart).padding(start = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("00:00", "04:00", "08:00", "12:00", "16:00", "20:00").forEach { time ->
                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Simple line representation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Blue500)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun RecentAccessActivitySection() {
    GlassBox {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Blue500
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("PENGGUNA", "PERAN", "AKSI", "METODE", "STATUS", "CATATAN", "WAKTU").forEach { header ->
                    Text(
                        text = header,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Box(
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
    PortalTikTheme {
        Scaffold { p ->
            DesktopDashboardScreen(
                contentPadding = PaddingValues(24.dp),
                modifier = Modifier.padding(p)
            )
        }
    }
}
