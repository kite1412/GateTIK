package kite1412.gatetik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedContainerColor: Color = Blue500,
    selectedContentColor: Color = White,
    unselectedContainerColor: Color = Color.Transparent,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val isDarkMode = LocalDarkMode.current
    val backgroundColor by animateColorAsState(
        if (isSelected) selectedContainerColor 
        else if (isDarkMode) White.copy(alpha = 0.05f) 
        else unselectedContainerColor
    )
    val contentColor by animateColorAsState(
        if (isSelected) selectedContentColor else unselectedContentColor
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor
        )
    }
}
