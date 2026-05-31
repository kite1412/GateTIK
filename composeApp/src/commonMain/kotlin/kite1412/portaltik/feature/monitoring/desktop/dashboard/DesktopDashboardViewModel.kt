package kite1412.portaltik.feature.monitoring.desktop.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.datastore.PortalTikDataStore
import kotlinx.coroutines.launch

class DesktopDashboardViewModel(
    private val dataStore: PortalTikDataStore
) : ViewModel() {
    fun updateDarkMode(value: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(value)
        }
    }
}