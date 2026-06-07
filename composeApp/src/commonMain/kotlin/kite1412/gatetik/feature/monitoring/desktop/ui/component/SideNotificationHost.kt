package kite1412.gatetik.feature.monitoring.desktop.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Blue500Alt
import kite1412.gatetik.designsystem.theme.Blue500Alt_30
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.RoyalBlue800_60
import kite1412.gatetik.designsystem.theme.Slate900
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotification
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.preview.PreviewPhoneDark
import kotlinx.coroutines.delay

@Composable
fun SideNotificationHost(
    notifications: List<SideNotification>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.End,
        userScrollEnabled = false
    ) {
        items(notifications) { notification ->
            SideNotification(
                notification = notification,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun SideNotification(
    notification: SideNotification,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current
) {
    val border by animateColorAsState(if (isDarkMode) RoyalBlue800_60 else Blue500Alt)
    val backgroundColor by animateColorAsState(if (isDarkMode) Slate900.copy(alpha = 0.8f) else Blue500Alt_30)
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .widthIn(min = 250.dp, max = 300.dp)
            .border(
                width = 2.dp,
                color = border,
                shape = shape
            )
            .background(
                color = backgroundColor,
                shape = shape
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        notification.leadingIcon?.invoke()
        Text(
            text = notification.message,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private val mockNotifications = listOf(
    SideNotification(
        id = "1",
        message = "User created successfully"
    ),
    SideNotification(
        id = "2",
        message = "Failed to connect to server",
        isAutoDismissed = false
    ),
    SideNotification(
        id = "3",
        message = "New update available",
        durationMs = 5000
    )
)

@Preview
@PreviewPhoneDark
@Composable
private fun SideNotificationHostPreview() {
    val notifications = remember {
        mutableStateListOf<SideNotification>()
    }

    LaunchedEffect(Unit) {
        mockNotifications.forEach {
            delay(3000)
            notifications.add(0, it)
        }
        mockNotifications.forEach {
            delay(3000)
            notifications.remove(it)
        }
    }
    CompositionLocalProvider(
        LocalDarkMode provides isSystemInDarkTheme()
    ) {
        GateTikTheme {
            Scaffold { p ->
                SideNotificationHost(
                    notifications = notifications,
                    modifier = Modifier.padding(p)
                )
            }
        }
    }
}