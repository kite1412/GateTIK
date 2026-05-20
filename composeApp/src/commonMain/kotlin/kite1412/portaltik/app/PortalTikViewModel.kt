package kite1412.portaltik.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PortalTikViewModel(
    dataStore: PortalTikDataStore
) : ViewModel() {
    var signedInUser: User? = null
        private set

    val isDarkMode = dataStore
        .observeDarkMode()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}