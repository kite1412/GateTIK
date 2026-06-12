package kite1412.gatetik.feature.monitoring.desktop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.data.util.Poller
import kite1412.gatetik.data.util.PollingAction
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotificationManager
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DesktopBaseViewModel(
    authentication: Authentication,
    private val dataStore: GateTikDataStore
) : ViewModel() {
    private var poller: Poller? = null
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

    protected fun polling(action: PollingAction) {
        if (poller == null) Poller(
            dataStore = dataStore,
            sideNotificationManager = sideNotificationManager
        )
            .also {
                poller = it
            }
            .pollWith(viewModelScope, action)
        else poller?.pollWith(viewModelScope, action)
    }

    protected fun <T, E: Error> Result<T, E>.toPollingResult(
        errorMessage: String
    ) = when (this) {
        is Result.Error<*> -> PollingResult.Error(errorMessage)
        is Result.Success<*> -> PollingResult.Success
        else -> throw IllegalArgumentException("Polling result must be either Success or Error")
    }
}

sealed interface PollingResult {
    object Success : PollingResult

    data class Error(val message: String) : PollingResult
}