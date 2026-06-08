package kite1412.gatetik.feature.monitoring.desktop

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotification
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotificationManager
import kite1412.gatetik.ui.component.SmallCircularProgressIndicator
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DesktopBaseViewModel(
    authentication: Authentication,
    private val dataStore: GateTikDataStore
) : ViewModel() {
    val signedInUser = authentication
        .sessionStatus
        .map { status ->
            if (status is SessionStatus.SignedIn)
                status.user
            else null
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    val sideNotificationManager = SideNotificationManager(viewModelScope)

    fun updateDarkMode(value: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(value)
        }
    }

    protected fun initPolling(block: suspend () -> PollingResult) {
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

                when (val res = block()) {
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
            .launchIn(viewModelScope)
    }

    protected fun <T, E: Error> Result<T, E>.toPollingResult(
        errorMessage: String
    ) = when (this) {
        is Result.Error<*> -> PollingResult.Error(errorMessage)
        is Result.Success<*> -> PollingResult.Success
        else -> throw IllegalArgumentException("Polling result must be either Success or Error")
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

sealed interface PollingResult {
    object Success : PollingResult

    data class Error(val message: String) : PollingResult
}