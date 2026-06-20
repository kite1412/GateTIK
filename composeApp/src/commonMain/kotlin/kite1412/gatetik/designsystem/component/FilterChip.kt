package kite1412.gatetik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
    leadingIcon: (@Composable () -> Unit)? = null,
    selectedContainerColor: Color = Blue500,
    selectedContentColor: Color = White,
    unselectedContainerColor: Color = Color.Transparent,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
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
            .clip(shape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            leadingIcon?.let {
                CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides contentColor
                ) {
                    it()
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}
