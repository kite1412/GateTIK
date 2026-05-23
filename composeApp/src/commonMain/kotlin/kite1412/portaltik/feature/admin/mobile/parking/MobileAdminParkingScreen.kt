package kite1412.portaltik.feature.admin.mobile.parking

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.SectionHeader
import kite1412.portaltik.designsystem.theme.Emerald500
import kite1412.portaltik.designsystem.theme.Emerald700
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Slate900
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.component.InfoCard
import kite1412.portaltik.ui.component.StatCard
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.ScaffoldComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MobileAdminParkingScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileAdminParkingViewModel = koinViewModel()
) {
    MobileAdminParkingScreen(
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileAdminParkingScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
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
            SectionHeader(
                title = "Parkir",
                subtitle = "Kuota Mahasiswa · Staf/Admin tidak terbatas"
            )
        }

        item {
            OccupancyChartCard(
                used = 72,
                total = 100
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "TOTAL",
                    value = "100",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "TERPAKAI",
                    value = "72",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "SISA",
                    value = "28",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoCard(
                    icon = painterResource(PortalTikIcons.car),
                    title = "Sistem Kuota Mahasiswa",
                    description = "Slot parkir terbatas untuk mahasiswa. Akses akan diblokir jika sudah penuh."
                )

                InfoCard(
                    icon = painterResource(PortalTikIcons.locationMark),
                    title = "Akses Staf & Admin",
                    description = "Akses parkir tidak terbatas, tidak tunduk pada batasan kapasitas.",
                    iconBackground = Emerald700.copy(alpha = 0.3f),
                    iconColor = Emerald700
                )
            }
        }
    }
}

@Composable
private fun OccupancyChartCard(
    used: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val occupancyPercent = (used.toFloat() / total.toFloat() * 100).toInt()
    val isDarkMode = LocalDarkMode.current

    GlassBox(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentPadding = PaddingValues(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val trackColor = if (isDarkMode) Slate900.copy(alpha = 0.5f) else Color(0xFFE2E8F0)
                
                CircularProgressIndicator(
                    progress = { used.toFloat() / total.toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = Emerald500,
                    strokeWidth = 14.dp,
                    trackColor = trackColor,
                    strokeCap = StrokeCap.Round
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TERISI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$occupancyPercent%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$used / $total slot",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Badge(
                text = "Tersedia",
                containerColor = Emerald500,
                contentColor = White,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun MobileAdminParkingScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
            MobileAdminParkingScreen(
                contentPadding = p
            )
        }
    }
}
