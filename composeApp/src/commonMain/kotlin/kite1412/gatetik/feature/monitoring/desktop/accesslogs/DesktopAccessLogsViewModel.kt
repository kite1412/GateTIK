package kite1412.gatetik.feature.monitoring.desktop.accesslogs

import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class DesktopAccessLogsViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore
) : DesktopBaseViewModel(authentication, dataStore) {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<AccessStatus?>(null)
    val selectedStatusFilter = _selectedStatusFilter.asStateFlow()

    private val _selectedMethodFilter = MutableStateFlow<AccessMethod?>(null)
    val selectedMethodFilter = _selectedMethodFilter.asStateFlow()

    private val _selectedActionFilter = MutableStateFlow<AccessAction?>(null)
    val selectedActionFilter = _selectedActionFilter.asStateFlow()

    private val _selectedSort = MutableStateFlow("Terbaru")
    val selectedSort = _selectedSort.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    private val _itemsPerPage = MutableStateFlow(15)
    val itemsPerPage = _itemsPerPage.asStateFlow()

    private val _accessLogs = MutableStateFlow(createDummyLogs())
    val accessLogs = _accessLogs.asStateFlow()

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun updateStatusFilter(status: AccessStatus?) {
        _selectedStatusFilter.value = status
    }

    fun updateMethodFilter(method: AccessMethod?) {
        _selectedMethodFilter.value = method
    }

    fun updateActionFilter(action: AccessAction?) {
        _selectedActionFilter.value = action
    }

    fun updateSort(sort: String) {
        _selectedSort.value = sort
    }

    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    fun updateItemsPerPage(count: Int) {
        _itemsPerPage.value = count
    }

    fun exportCsv() {
        // Implement export logic or just a print for now
        println("Exporting CSV...")
    }

    private fun createDummyLogs(): List<AccessLog> {
        val now = Clock.System.now()
        return listOf(
            AccessLog(
                id = 1,
                userId = 1,
                gateId = 1,
                userFullName = "Username",
                userRole = UserRole.ADMIN,
                status = AccessStatus.FAILED,
                accessMethod = AccessMethod.MOBILE,
                action = AccessAction.ENTRY,
                notes = "User is outside the allowed gate radius. Access denied.",
                updatedAt = now,
                createdAt = now
            ),
            AccessLog(
                id = 2,
                userId = 1,
                gateId = 1,
                userFullName = "Username",
                userRole = UserRole.ADMIN,
                status = AccessStatus.FAILED,
                accessMethod = AccessMethod.MOBILE,
                action = AccessAction.ENTRY,
                notes = "User is outside the allowed gate radius. Access denied.",
                updatedAt = now.minus(5.minutes),
                createdAt = now.minus(5.minutes)
            )
        )
    }
}
