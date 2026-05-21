package kite1412.portaltik.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.Authentication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PortalTikViewModel(
    dataStore: PortalTikDataStore,
    authentication: Authentication,
) : ViewModel() {
    val signedInUser = authentication.signedInUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val isDarkMode = dataStore
        .observeDarkMode()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}