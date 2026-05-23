package kite1412.portaltik.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.Blue200_50
import kite1412.portaltik.designsystem.theme.Slate900_95
import kite1412.portaltik.designsystem.theme.White55
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

@Composable
fun InfoCard(
    icon: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    iconBackground: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val isDarkMode = LocalDarkMode.current
    val containerColor by animateColorAsState(
        targetValue = if (isDarkMode) Slate900_95 else White55
    )
    val shape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .run {
                if (!isDarkMode) border(
                    width = 2.dp,
                    color = Blue200_50,
                    shape = shape
                ) else this
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = iconBackground,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
