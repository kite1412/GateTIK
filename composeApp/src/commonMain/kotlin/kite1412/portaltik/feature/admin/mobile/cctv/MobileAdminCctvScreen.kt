package kite1412.portaltik.feature.admin.mobile.cctv

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.component.SectionHeader
import kite1412.portaltik.designsystem.theme.Black
import kite1412.portaltik.designsystem.theme.Emerald700
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Red600
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White20
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.component.InfoCard
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.ScaffoldComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MobileAdminCctvScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileAdminCctvViewModel = koinViewModel()
) {
    MobileAdminCctvScreen(
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileAdminCctvScreen(
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
                title = "CCTV",
                subtitle = "Kamera Gerbang Utama · Interkom juga memiliki kamera"
            )
        }
        item {
            CctvPlayer(
                label = "CAM-01 · Gerbang Utama"
            )
        }
        item {
            InfoCard(
                icon = painterResource(PortalTikIcons.phone),
                title = "Kamera Interkom",
                description = "Sistem interkom pengunjung memiliki kamera internal untuk identifikasi visual saat panggilan."
            )
        }
    }
}

@Composable
private fun CctvPlayer(
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color(0xFF0F172A)) // Dark slate for preview
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val infiniteTransition = rememberInfiniteTransition()
                val iconColor by infiniteTransition.animateColor(
                    initialValue = Emerald700,
                    targetValue = Emerald700.copy(alpha = 0.4f),
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Badge(
                        text = "LIVE",
                        containerColor = Red600,
                        contentColor = White
                    )

                    Badge(
                        text = label,
                        containerColor = White20,
                        contentColor = White
                    )
                }

                Icon(
                    painter = painterResource(PortalTikIcons.radio),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(PortalTikIcons.soundUp),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = White
            )
            Icon(
                painter = painterResource(PortalTikIcons.camera),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = White
            )
            Icon(
                painter = painterResource(PortalTikIcons.zoomIn),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = White
            )
        }
    }
}

@DevicePreviews
@Composable
private fun MobileAdminCctvScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              MobileAdminCctvScreen(
                  contentPadding = p
              )
        }
    }
}
