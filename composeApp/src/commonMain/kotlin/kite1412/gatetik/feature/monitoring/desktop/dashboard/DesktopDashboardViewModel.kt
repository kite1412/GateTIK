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
import kite1412.gatetik.domain.usecase.GetMainCctvUseCase
import kite1412.gatetik.domain.usecase.GetMainGateUseCase
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
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
    getMainCctvUseCase: GetMainCctvUseCase,
    private val userRepository: UserRepository,
    private val accessLogRepository: AccessLogRepository,
    private val accessGateUseCase: AccessGateUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val gate = getMainGateUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    val parkingQuota = getMainParkingQuotaUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    val cctv = getMainCctvUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    var totalUsers by mutableStateOf<LoadState<Int>>(LoadState.Loading())
        private set
    var accessLogs by mutableStateOf<LoadState<List<AccessLog>>>(LoadState.Loading())
        private set

    init {
        initTotalUsers()
        initAccessLogs()
    }

    fun openGate() {
        viewModelScope.launch {
            gate.first().data?.let { gate ->
                accessGateUseCase.open(gate.id)
                    .onSuccess { success ->
                        _uiEvent.emit(
                            UiEvent.ShowSnackbar(
                                if (success) "Gate dibuka"
                                else "Gagal membuka gate"
                            )
                        )
                    }
                    .onError {
                        _uiEvent.emit(UiEvent.ShowSnackbar("Gagal membuka gate"))
                    }
            }
        }
    }

    fun closeGate() {
        viewModelScope.launch {
            gate.first().data?.let { gate ->
                accessGateUseCase.close(gate.id)
                    .onSuccess { success ->
                        _uiEvent.emit(
                            UiEvent.ShowSnackbar(
                                if (success) "Gate ditutup"
                                else "Gagal menutup gate"
                            )
                        )
                    }
                    .onError {
                        _uiEvent.emit(UiEvent.ShowSnackbar("Gagal menutup gate"))
                    }
            }
        }
    }

    private fun initTotalUsers() {
        viewModelScope.launch {
            userRepository.getAll()
                .onSuccess {
                    totalUsers = LoadState.Success(it.data.size)
                }
                .onError {
                    totalUsers = LoadState.Error("Gagal memuat informasi pengguna")
                }
        }
    }

    private fun initAccessLogs() {
        viewModelScope.launch {
            accessLogRepository.getAll()
                .onSuccess {
                    accessLogs = LoadState.Success(it.data)
                }
                .onError {
                    accessLogs = LoadState.Error("Gagal memuat log akses")
                }
        }
    }
}