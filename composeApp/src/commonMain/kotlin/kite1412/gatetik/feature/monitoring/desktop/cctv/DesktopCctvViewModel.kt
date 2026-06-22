package kite1412.gatetik.feature.monitoring.desktop.cctv

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.domain.repository.CctvRepository
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class CctvTab {
    MONITOR, MANAGE
}

class DesktopCctvViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    private val cctvRepository: CctvRepository
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(CctvTab.MONITOR)
    val selectedTab = _selectedTab.asStateFlow()

    private val _gridColumns = MutableStateFlow(2)
    val gridColumns = _gridColumns.asStateFlow()

    private val openCctvWindows = mutableStateMapOf<String, Boolean>()
    var cctvs by mutableStateOf<LoadState<List<Cctv>>>(LoadState.Loading("Memuat Cctv"))
        private set

    init {
        viewModelScope.launch {
            updateCctvs()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedTab(tab: CctvTab) {
        _selectedTab.value = tab
    }

    fun updateGridColumns(columns: Int) {
        _gridColumns.value = columns
    }

    fun addCctv(data: CctvCreate) {
        viewModelScope.launch {
            cctvRepository
                .addCctv(data)
                .onSuccess {
                    cctvs = LoadState.Success((cctvs.data ?: emptyList()) + listOf(it))
                    _uiEvent.emit(UiEvent.ShowSnackbar("Berhasil menambah CCTV"))
                }
        }
    }

    fun updateCctv(data: CctvUpdate) {
        viewModelScope.launch {
            cctvRepository
                .updateCctv(data)
                .onSuccess { cctv ->
                    cctvs.data?.indexOfFirst { it.id == cctv.id }
                        ?.takeIf { it != -1 }
                        ?.let { index ->
                            cctvs.data?.toMutableList()?.apply {
                                set(index, cctv)
                            }
                                ?.let {
                                    cctvs = LoadState.Success(it)
                                    _uiEvent.emit(UiEvent.ShowSnackbar("CCTV diperbarui"))
                                }
                        }
                }
        }
    }

    fun deleteCctv(id: Int) {
        viewModelScope.launch {
            cctvRepository
                .deleteCctv(id)
                .onSuccess {
                    cctvs.data?.let { cctvs ->
                        cctvs.indexOfFirst { it.id == id }
                            .takeIf { it != -1 }
                            ?.let { index ->
                                this@DesktopCctvViewModel.cctvs = LoadState.Success(
                                    cctvs.toMutableList().apply {
                                        removeAt(index)
                                    }
                                )
                                _uiEvent.emit(UiEvent.ShowSnackbar("CCTV dihapus"))
                            }
                    }
                }
        }
    }

    fun refreshCctvs() {
        viewModelScope.launch {
            updateCctvs()
            _uiEvent.emit(UiEvent.ShowSnackbar("Data dimuat ulang"))
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

    private suspend fun updateCctvs() {
        cctvRepository
            .getAll()
            .onSuccess {
                cctvs = LoadState.Success(it)
            }
            .onError {
                cctvs = LoadState.Error("Gagal memuat cctv")
            }
    }
}
