package kite1412.portaltik.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.Blue200
import kite1412.portaltik.designsystem.theme.Blue200_50
import kite1412.portaltik.designsystem.theme.Emerald500
import kite1412.portaltik.designsystem.theme.Slate400
import kite1412.portaltik.designsystem.theme.Slate500
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White55
import kite1412.portaltik.designsystem.util.GateTikIcons
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.util.LoadState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ParkingQuotaCard(
    parkingQuota: LoadState<ParkingQuota?>,
    modifier: Modifier = Modifier,
    icon: DrawableResource = GateTikIcons.car,
    isDarkMode: Boolean = LocalDarkMode.current
) {
    val background = if (isDarkMode) MaterialTheme.colorScheme.surface.copy(alpha = 0.7f) else White55
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
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
                    painter = painterResource(icon),
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