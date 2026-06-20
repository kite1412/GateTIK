package kite1412.gatetik.feature.monitoring.desktop.cctv

import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.network.mock.mockCctvs
import kite1412.gatetik.util.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CctvTab {
    MONITOR, KELOLA
}

class DesktopCctvViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore) {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(CctvTab.MONITOR)
    val selectedTab = _selectedTab.asStateFlow()

    private val _gridColumns = MutableStateFlow(2)
    val gridColumns = _gridColumns.asStateFlow()

    private val _cameras = MutableStateFlow(mockCctvs)
    val cameras = _cameras.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedTab(tab: CctvTab) {
        _selectedTab.value = tab
    }

    fun updateGridColumns(columns: Int) {
        _gridColumns.value = columns
    }

    fun addCamera(name: String, path: String, url: String) {
        _cameras.update {
            it + Cctv(
                id = (it.maxOfOrNull { c -> c.id } ?: 0) + 1,
                cameraName = name,
                streamUrl = url,
                path = path,
                isActive = true,
                type = CctvType.MONITOR,
                createdAt = now()
            )
        }
    }

    fun deleteCamera(camera: Cctv) {
        _cameras.update { it.filter { c -> c.id != camera.id } }
    }
}
