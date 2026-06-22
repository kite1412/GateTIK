package kite1412.gatetik.feature.monitoring.desktop.cctv.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.util.GateTikIcons
import org.jetbrains.compose.resources.painterResource

@Composable
fun CctvStatsCard(
    label: String,
    value: String,
    icon: Painter,
    iconContainerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconContainerColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconTint
                )
            }

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun CctvStatsCardPreview() {
    GateTikTheme {
        CctvStatsCard(
            label = "Online",
            value = "2 Kamera",
            icon = painterResource(GateTikIcons.wifi),
            iconContainerColor = Emerald500.copy(alpha = 0.1f),
            iconTint = Emerald500,
            modifier = Modifier.padding(16.dp)
        )
    }
}
