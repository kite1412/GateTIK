package kite1412.gatetik.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.theme.Blue200
import kite1412.gatetik.designsystem.theme.Blue900
import kite1412.gatetik.designsystem.theme.Slate400
import kite1412.gatetik.designsystem.theme.Slate500
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.util.now
import kite1412.gatetik.util.toLocalDateTime

@Composable
fun HeaderSection(
    userName: String,
    userRole: UserRole,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    val currentHour = now().toLocalDateTime().hour
    val greeting = when (currentHour) {
        in 6..11 -> "Selamat pagi,"
        in 12..15 -> "Selamat siang,"
        in 16..18 -> "Selamat sore,"
        else -> "Selamat malam,"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDarkMode) Slate400 else Slate500
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Badge(
                text = userRole.toIdString().uppercase(),
                containerColor = if (isDarkMode) Blue900.copy(alpha = 0.4f) else Blue200.copy(alpha = 0.4f),
                contentColor = if (isDarkMode) Blue200 else Blue900
            )

            trailing?.invoke()
        }
    }
}