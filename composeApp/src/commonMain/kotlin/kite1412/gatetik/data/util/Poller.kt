package kite1412.gatetik.data.util

import androidx.compose.runtime.Composable
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.feature.monitoring.desktop.PollingResult
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotification
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotificationManager
import kite1412.gatetik.ui.component.SmallCircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds

class Poller(
    private val dataStore: GateTikDataStore,
    private val sideNotificationManager: SideNotificationManager
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun pollWith(scope: CoroutineScope, action: PollingAction) {
        combine(
            dataStore.observePollingEnabled(),
            dataStore.observePollingIntervalMs()
        ) { enabled, intervalMs ->
            enabled to intervalMs
        }
            .flatMapLatest { (enabled, intervalMs) ->
                if (!enabled) emptyFlow()
                else {
                    flow {
                        while (currentCoroutineContext().isActive) {
                            delay(intervalMs.toLong().milliseconds)
                            emit(Unit)
                        }
                    }
                }
            }
            .onEach {
                sideNotificationManager.notify(
                    createPollingNotification(
                        message = "Polling data...",
                        isAutoDismissed = false,
                        leadingIcon = { SmallCircularProgressIndicator() }
                    )
                )

                when (val res = action.execute()) {
                    is PollingResult.Success -> {
                        sideNotificationManager.notify(
                            createPollingNotification(
                                message = "Polling data berhasil"
                            )
                        )
                    }
                    is PollingResult.Error -> {
                        sideNotificationManager.notify(
                            createPollingNotification(
                                message = res.message
                            )
                        )
                    }
                }
            }
            .launchIn(scope)
    }

    private fun createPollingNotification(
        message: String,
        isAutoDismissed: Boolean = true,
        leadingIcon: (@Composable () -> Unit)? = null
    ) = SideNotification(
        id = "polling",
        message = message,
        isAutoDismissed = isAutoDismissed,
        leadingIcon = leadingIcon
    )
}

fun interface PollingAction {
    suspend fun execute(): PollingResult
}