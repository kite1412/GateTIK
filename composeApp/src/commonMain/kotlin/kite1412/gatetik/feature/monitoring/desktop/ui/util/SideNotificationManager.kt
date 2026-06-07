package kite1412.gatetik.feature.monitoring.desktop.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SideNotificationManager(
    private val coroutineScope: CoroutineScope
) {
    val notifications = mutableStateListOf<SideNotification>()

    fun notify(notification: SideNotification) {
        coroutineScope.launch {
            val index = notifications.indexOfFirst { it.id == notification.id }

            if (index == -1) notifications.add(0, notification)
            else notifications[index] = notification

            if (notification.isAutoDismissed) {
                delay(notification.durationMs)
                notifications.remove(notification)
            }
        }
    }

    fun dismiss(id: String) {
        notifications
            .indexOfFirst { it.id == id }
            .takeIf { it != -1 }
            ?.let(notifications::removeAt)
    }
}

data class SideNotification(
    val id: String,
    val message: String,
    val isAutoDismissed: Boolean = true,
    val durationMs: Long = 3000,
    val leadingIcon: (@Composable () -> Unit)? = null
)