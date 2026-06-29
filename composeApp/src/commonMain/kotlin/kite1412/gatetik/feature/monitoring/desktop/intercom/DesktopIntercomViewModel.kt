package kite1412.gatetik.feature.monitoring.desktop.intercom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.usecase.GetCctvUseCase
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.showSnackbar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DesktopIntercomViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    private val getCctvUseCase: GetCctvUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val openCctvWindows = mutableStateMapOf<String, Boolean>()
    
    var intercomCameras by mutableStateOf<LoadState<List<Cctv>>>(LoadState.Loading("Memuat Interkom"))
        private set

    init {
        loadIntercomCameras()
    }

    private fun loadIntercomCameras() {
        getCctvUseCase.observeAllAsLoadStateFlow()
            .onEach { state ->
                intercomCameras = if (state is LoadState.Success) {
                    LoadState.Success(state.data.filter { it.type == CctvType.INTERCOM })
                } else {
                    state
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _currentPage.value = 0
    }

    fun updateCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun refreshIntercomCameras() {
        viewModelScope.launch {
            loadIntercomCameras()
            _uiEvent.showSnackbar("Data dimuat ulang")
        }
    }

    fun isCctvWindowOpen(cctv: Cctv) = openCctvWindows.containsKey(cctv.path)

    fun shouldAutoMicOn(cctv: Cctv) = openCctvWindows[cctv.path] ?: false

    fun openCctvWindow(cctv: Cctv, autoMicOn: Boolean = false) {
        openCctvWindows[cctv.path] = autoMicOn
    }

    fun closeCctvWindow(cctv: Cctv) {
        openCctvWindows.remove(cctv.path)
    }
}
