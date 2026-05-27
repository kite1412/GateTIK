package kite1412.portaltik.feature.admin.mobile.gate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.repository.AccessLogRepository
import kite1412.portaltik.domain.usecase.CloseGateUseCase
import kite1412.portaltik.domain.usecase.GetMainGateUseCase
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.domain.usecase.OpenGateUseCase
import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.model.AccessStatus
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.stateIn
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MobileAdminGateViewModel(
    getMainGateUseCase: GetMainGateUseCase,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    private val accessLogRepository: AccessLogRepository,
    private val openGateUseCase: OpenGateUseCase,
    private val closeGateUseCase: CloseGateUseCase
) : ViewModel() {
    private var gateId = 0
    val mainGate = getMainGateUseCase()
        .onEach {
            if (it is LoadState.Success && it.data != null)
                gateId = it.data.id
        }
        .stateIn(viewModelScope)
    val mainParkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)

    var latestAccessLog by mutableStateOf<LoadState<AccessLog?>>(LoadState.Loading())
        private set

    init {
        viewModelScope.launch {
            getLatestAccessLog()
        }
    }

    fun openGate() {
        viewModelScope.launch {
            openGateUseCase(gateId)
        }
    }

    fun closeGate() {
        viewModelScope.launch {
            closeGateUseCase(gateId)
        }
    }

    private suspend fun getLatestAccessLog() {
        accessLogRepository.getLatestLogs()
            .onError {
                latestAccessLog = LoadState.Error("Gagal Mencari Informasi.")
            }
            .onSuccess { logs ->
                latestAccessLog = LoadState.Success(
                    data = logs
                        .filter { it.status == AccessStatus.SUCCESS }
                        .maxByOrNull { it.accessedAt }
                )
            }
    }
}