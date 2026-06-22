package kite1412.gatetik.feature.monitoring.desktop.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.repository.AccessLogRepository
import kite1412.gatetik.domain.repository.UserRepository
import kite1412.gatetik.domain.usecase.AccessGateUseCase
import kite1412.gatetik.domain.usecase.GetCctvUseCase
import kite1412.gatetik.domain.usecase.GetMainGateUseCase
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.feature.monitoring.desktop.PollingResult
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.ui.util.showSnackbar
import kite1412.gatetik.ui.util.stateIn
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DesktopDashboardViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    getMainGateUseCase: GetMainGateUseCase,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    getCctvUseCase: GetCctvUseCase,
    private val userRepository: UserRepository,
    private val accessLogRepository: AccessLogRepository,
    private val accessGateUseCase: AccessGateUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val gate = getMainGateUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    val parkingQuota = getMainParkingQuotaUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    val cctvs = getCctvUseCase.observeAllAsLoadStateFlow().stateIn(viewModelScope)
    var totalUsers by mutableStateOf<LoadState<Int>>(LoadState.Loading())
        private set
    var accessLogs by mutableStateOf<LoadState<List<AccessLog>>>(LoadState.Loading())
        private set
    var fullScreenCctv by mutableStateOf<Cctv?>(null)
        private set
    var isFullScreenCctvAutoMicOn by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            pollData()
        }
        polling(::pollData)
    }

    fun openGate() {
        viewModelScope.launch {
            gate.first().data?.let { gate ->
                accessGateUseCase.open(gate.id)
                    .onSuccess { success ->
                        _uiEvent.showSnackbar(
                            if (success) "Gate dibuka"
                            else "Gagal membuka gate"
                        )
                    }
                    .onError {
                        _uiEvent.showSnackbar("Gagal membuka gate")
                    }
            }
        }
    }

    fun closeGate() {
        viewModelScope.launch {
            gate.first().data?.let { gate ->
                accessGateUseCase.close(gate.id)
                    .onSuccess { success ->
                        _uiEvent.showSnackbar(
                            if (success) "Gate ditutup"
                            else "Gagal menutup gate"
                        )
                    }
                    .onError {
                        _uiEvent.showSnackbar("Gagal menutup gate")
                    }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            pollData()
            _uiEvent.showSnackbar("Data dimuat ulang")
        }
    }

    fun updateFullScreenCctv(cctv: Cctv?, autoMicOn: Boolean) {
        isFullScreenCctvAutoMicOn = autoMicOn
        fullScreenCctv = cctv
    }

    private suspend fun pollData() = listOf(
        updateTotalUsers().toPollingResult("Gagal memperbarui data pengguna"),
        updateAccessLogs().toPollingResult("Gagal memperbarui data akses log")
    )
        .firstOrNull { it is PollingResult.Error }
        ?: PollingResult.Success

    private suspend fun updateTotalUsers() = userRepository.getAll()
        .onSuccess {
            totalUsers = LoadState.Success(it.pagination.total)
        }
        .onError {
            totalUsers = LoadState.Error("Gagal memuat informasi pengguna")
        }

    private suspend fun updateAccessLogs() = accessLogRepository.getAll(
        params = AccessLogRepository.GetParams(
            period = AccessLogRepository.LogPeriod.DAY,
            perPage = 100
        )
    )
        .onSuccess {
            accessLogs = LoadState.Success(it.data)
        }
        .onError {
            accessLogs = LoadState.Error("Gagal memuat log akses")
        }
}