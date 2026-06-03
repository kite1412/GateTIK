package kite1412.gatetik.feature.monitoring.desktop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun updateDarkMode(value: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(value)
        }
    }
}