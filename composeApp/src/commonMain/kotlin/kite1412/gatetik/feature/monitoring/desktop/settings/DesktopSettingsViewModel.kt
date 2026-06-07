package kite1412.gatetik.feature.monitoring.desktop.settings

import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.ui.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DesktopSettingsViewModel(
    authentication: Authentication,
    private val dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val isPollingEnabled = dataStore
        .observePollingEnabled()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GateTikDataStore.DEFAULT_POLLING_ENABLED
        )

    val pollingIntervalSecond = dataStore
        .observePollingIntervalMs()
        .map { it / 1000 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GateTikDataStore.DEFAULT_POLLING_INTERVAL_MS
        )

    fun updatePollingEnabled(value: Boolean) {
        viewModelScope.launch {
            dataStore.setPollingEnabled(value)
        }
    }

    fun updatePollingIntervalSecond(second: Int) {
        viewModelScope.launch {
            dataStore.setPollingIntervalMs(second * 1000)
            _uiEvent.emit(
                UiEvent.ShowSnackbar("Berhasil memperbarui Polling Interval")
            )
        }
    }
}