package kite1412.gatetik.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.app.util.AppScaffoldComponentsController
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.AppVersion
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.domain.VersionChecker
import kite1412.gatetik.ui.util.ScaffoldComponent
import kite1412.gatetik.ui.util.ScaffoldComponentState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GateTikViewModel(
    val versionChecker: VersionChecker,
    private val dataStore: GateTikDataStore,
    private val authentication: Authentication
) : ViewModel() {
    private val scaffoldComponentStates = mutableStateMapOf<ScaffoldComponent, ScaffoldComponentState>()
    val sessionStatus = authentication.sessionStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionStatus.Loading
        )
    val isDarkMode = dataStore
        .observeDarkMode()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val isFirstLaunch = dataStore
        .observeFirstLaunch()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val scaffoldComponentsController = AppScaffoldComponentsController(scaffoldComponentStates)
    var appLatestVersion by mutableStateOf<AppVersion?>(null)
        private set
    var showAppUpdateNotification by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            appLatestVersion = versionChecker.getAppLatestVersion()
            appLatestVersion?.let {
                showAppUpdateNotification = versionChecker.hasUpdate(it)
            }
        }
    }

    fun completeFirstLaunch() {
        viewModelScope.launch {
            dataStore.setFirstLaunch(false)
        }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            authentication.logout()
        }
    }

    fun dismissAppUpdateNotification() {
        showAppUpdateNotification = false
    }
}