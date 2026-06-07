package kite1412.gatetik.feature.monitoring.desktop.accesslogs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.CsvExportResult
import kite1412.gatetik.CsvExporter
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.repository.AccessLogRepository
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.feature.monitoring.desktop.accesslogs.util.Sort
import kite1412.gatetik.feature.monitoring.desktop.accesslogs.util.writeToCsv
import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.util.now
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kite1412.gatetik.util.timestampString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DesktopAccessLogsViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    private val accessLogRepository: AccessLogRepository
) : DesktopBaseViewModel(authentication, dataStore) {
    private val logStore = mutableStateMapOf<AccessLogRepository.GetParams, PaginatedListResult<AccessLog>>()
    private var trendAccessLogStore: List<AccessLog> = emptyList()
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var currentPage by mutableIntStateOf(1)
        private set
    var perPage by mutableIntStateOf(15)
        private set
    var searchText by mutableStateOf<String?>(null)
        private set
    var selectedStatusFilter by mutableStateOf<AccessStatus?>(null)
        private set
    var selectedTrendStatusFilter by mutableStateOf<AccessStatus?>(null)
        private set
    var selectedMethodFilter by mutableStateOf<AccessMethod?>(null)
        private set
    var selectedActionFilter by mutableStateOf<AccessAction?>(null)
        private set
    var selectedSort by mutableStateOf(Sort.ASC)
        private set
    var accessLogs by mutableStateOf<LoadState<List<AccessLog>>>(LoadState.Loading("Memuat log akses"))
        private set
    var trendAccessLogs by mutableStateOf<LoadState<List<AccessLog>>>(LoadState.Loading("Memuat log akses"))
        private set

    private val params = snapshotFlow {
        AccessLogRepository.GetParams(
            status = selectedStatusFilter,
            method = selectedMethodFilter,
            action = selectedActionFilter,
            search = searchText,
            page = currentPage,
            perPage = perPage,
            isDescending = selectedSort == Sort.DESC
        )
    }
        .distinctUntilChanged()
        .onEach(::updateLogsOnParamsChange)
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

    init {
        viewModelScope.launch {
            accessLogRepository.getAll(
                params = AccessLogRepository.GetParams(
                    page = 1,
                    perPage = 100,
                    isDescending = true
                )
            )
                .onSuccess {
                    trendAccessLogStore = it.data
                    trendAccessLogs = LoadState.Success(it.data)
                }
                .onError {
                    trendAccessLogs = LoadState.Error("Gagal memuat log akses")
                }

            snapshotFlow { selectedTrendStatusFilter }
                .onEach { status ->
                    trendAccessLogs = LoadState.Success(
                        if (status != null) trendAccessLogStore.filter { log ->
                            log.status == status
                        } else trendAccessLogStore
                    )
                }
                .launchIn(this)
        }
    }

    fun updateSearchText(text: String) {
        searchText = text
    }

    fun updateStatusFilter(status: AccessStatus?) {
        selectedStatusFilter = status
    }

    fun updateTrendStatusFilter(status: AccessStatus?) {
        selectedTrendStatusFilter = status
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

    fun updateCurrentPage(page: Int) {
        currentPage = page
    }

    fun updatePerPage(perPage: Int) {
        this.perPage = perPage
        updateCurrentPage(1)
    }

    fun exportCsv(exporter: CsvExporter) {
        viewModelScope.launch {
            logStore
                .flatMap { (_, logResult) ->
                    logResult.data
                }
                .distinctBy { it.id }
                .sortedByDescending { it.createdAt }
                .takeIf { it.isNotEmpty() }
                ?.let { logs ->
                    val res = exporter.export(
                        filename = "GateTIK-access-logs(${
                            now().timestampString
                                .replace(oldChar = ' ', newChar = '-')
                                .replace(oldValue = ",", newValue = "")
                                .replace(oldChar = ':', newChar = '_')
                        })",
                        write = { logs.writeToCsv(writer = this) }
                    )

                    _uiEvent.emit(
                        UiEvent.ShowSnackbar(
                            when (res) {
                                is CsvExportResult.Cancelled -> res.reason
                                is CsvExportResult.Failed -> res.reason
                                is CsvExportResult.Success -> "Berhasil mengekspor data log akses"
                            }
                        )
                    )
                }
        }
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
