package kite1412.portaltik.app

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.app.util.AppScaffoldComponentsController
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.ScaffoldComponentState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PortalTikViewModel(
    dataStore: PortalTikDataStore,
    authentication: Authentication,
) : ViewModel() {
    private val scaffoldComponentStates = mutableStateMapOf<ScaffoldComponent, ScaffoldComponentState>()
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
    val scaffoldComponentsController = AppScaffoldComponentsController(scaffoldComponentStates)
}