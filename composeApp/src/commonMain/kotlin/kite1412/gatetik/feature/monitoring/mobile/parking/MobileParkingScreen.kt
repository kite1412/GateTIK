package kite1412.gatetik.feature.monitoring.mobile.parking

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.SectionHeader
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.Emerald700
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Slate900
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.ui.component.InfoCard
import kite1412.gatetik.ui.component.StatCard
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.LoadingState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.navBarPadding
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.max

@Composable
fun MobileParkingScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileParkingViewModel = koinViewModel()
) {
    val mainParkingQuota by viewModel.mainParkingQuota.collectAsStateWithLifecycle()

    MobileParkingScreen(
        parkingQuota = mainParkingQuota,
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileParkingScreen(
    parkingQuota: LoadState<ParkingQuota?>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scaffoldComponentsController = LocalScaffoldComponentsController.current
    val arrangement = Arrangement.spacedBy(24.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = navBarPadding(),
            verticalArrangement = arrangement
        ) {
            item {
                SectionHeader(
                    title = "Parkir",
                    subtitle = "Kuota Mahasiswa · Staf/Admin tidak terbatas"
                )
            }

            if (parkingQuota is LoadState.Success && parkingQuota.data != null) {
                val data = parkingQuota.data

                item {
                    OccupancyChartCard(
                        used = data.usedSlots,
                        total = data.totalSlots
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "TOTAL",
                            value = data.totalSlots.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "TERPAKAI",
                            value = data.usedSlots.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "SISA",
                            value = max(data.totalSlots - data.usedSlots, 0).toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoCard(
                            icon = painterResource(GateTikIcons.car),
                            title = "Sistem Kuota Mahasiswa",
                            description = "Slot parkir terbatas untuk mahasiswa. Akses akan diblokir jika sudah penuh."
                        )

                        InfoCard(
                            icon = painterResource(GateTikIcons.locationMark),
                            title = "Akses Staf & Admin",
                            description = "Akses parkir tidak terbatas, tidak tunduk pada batasan kapasitas.",
                            iconBackground = Emerald700.copy(alpha = 0.2f),
                            iconColor = Emerald700
                        )
                    }
                }
            }
        }
        if (parkingQuota !is LoadState.Success) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (parkingQuota is LoadState.Loading) LoadingState(
                    message = parkingQuota.message,
                    modifier = Modifier.align(Alignment.Center)
                ) else Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = MaterialTheme.colorScheme.outline

                    Icon(
                        painter = painterResource(GateTikIcons.parkingOff),
                        contentDescription = "gagal memuat informasi parkir",
                        modifier = Modifier.size(120.dp),
                        tint = color
                    )
                    Text(
                        text = "Gagal memuat informasi parkir",
                        style = MaterialTheme.typography.bodyLarge,
                        color = color,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold
                    )
                }
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
    val occupancyPercentage = if (used != 0 && total != 0) (used.toFloat() / total.toFloat() * 100).toInt()
        else 0
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
                    progress = { if (occupancyPercentage != 0) (occupancyPercentage / 100f) else 0f },
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
                        text = "$occupancyPercentage%",
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
private fun MobileParkingScreenPreview() {
    GateTikTheme(darkMode = isSystemInDarkTheme()) {
        Scaffold { p ->
            CompositionLocalProvider(
                LocalScaffoldComponentsController provides MockScaffoldComponentController
            ) {
                MobileParkingScreen(
                    parkingQuota = LoadState.Error("Error"),
                    contentPadding = p,
                    modifier = Modifier.radialBackground()
                )
            }
        }
    }
}
