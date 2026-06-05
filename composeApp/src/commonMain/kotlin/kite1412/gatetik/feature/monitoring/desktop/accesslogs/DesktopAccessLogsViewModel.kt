package kite1412.gatetik.feature.monitoring.desktop.accesslogs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.repository.AccessLogRepository
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.feature.monitoring.desktop.accesslogs.util.Sort
import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DesktopAccessLogsViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    private val accessLogRepository: AccessLogRepository
) : DesktopBaseViewModel(authentication, dataStore) {
    private val logStore = mutableStateMapOf<AccessLogRepository.GetParams, PaginatedListResult<AccessLog>>()
    var searchText by mutableStateOf("")
        private set

    var selectedStatusFilter by mutableStateOf<AccessStatus?>(null)
        private set

    var selectedMethodFilter by mutableStateOf<AccessMethod?>(null)
        private set

    var selectedActionFilter by mutableStateOf<AccessAction?>(null)
        private set

    var selectedSort by mutableStateOf(Sort.ASC)
        private set

    val params = combine(
        flow = snapshotFlow { selectedStatusFilter },
        flow2 = snapshotFlow { selectedMethodFilter },
        flow3 = snapshotFlow { selectedActionFilter },
        flow4 = snapshotFlow { searchText }
    ) { status, method, action, search ->
        val params = AccessLogRepository.GetParams(
            status = status,
            method = method,
            action = action,
            search = search
        )
        updateLogsOnParamsChange(params)
        params
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val pagination = combine(
        flow = snapshotFlow { logStore },
        flow2 = params
    ) { logStore, params ->
        logStore[params]?.pagination
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    var accessLogs by mutableStateOf<LoadState<List<AccessLog>>>(LoadState.Loading("Memuat log akses"))
        private set

    fun updateSearchText(text: String) {
        searchText = text
    }

    fun updateStatusFilter(status: AccessStatus?) {
        selectedStatusFilter = status
    }

    fun updateMethodFilter(method: AccessMethod?) {
        selectedMethodFilter = method
    }

    fun updateActionFilter(action: AccessAction?) {
        selectedActionFilter = action
    }

    fun updateSort(sort: Sort) {
        selectedSort = sort
    }

    fun exportCsv() {
        println("Exporting CSV...")
    }

    private suspend fun updateLogsOnParamsChange(params: AccessLogRepository.GetParams) {
        if (logStore[params] != null)
            accessLogs = LoadState.Success(logStore[params]!!.data)
        else {
            accessLogs = LoadState.Loading("Memuat log akses")
            accessLogRepository.getAll(params)
                .onSuccess {
                    accessLogs = LoadState.Success(it.data)
                    logStore[params] = it
                }
                .onError {
                    accessLogs = LoadState.Error("Gagal memuat log akses")
                }
        }
    }
}
