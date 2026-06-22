package kite1412.gatetik.feature.monitoring.mobile.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.domain.repository.AccessLogRepository
import kite1412.gatetik.domain.usecase.AccessGateUseCase
import kite1412.gatetik.domain.usecase.GetCctvUseCase
import kite1412.gatetik.domain.usecase.GetMainGateUseCase
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.showSnackbar
import kite1412.gatetik.ui.util.stateIn
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MobileHomeViewModel(
    authentication: Authentication,
    getMainGateUseCase: GetMainGateUseCase,
    getCctvUseCase: GetCctvUseCase,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    accessLogRepository: AccessLogRepository,
    private val accessGateUseCase: AccessGateUseCase
) : ViewModel() {
    private var gateId = 0
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    @OptIn(ExperimentalCoroutinesApi::class)
    val signedInUser = authentication.sessionStatus
        .mapLatest { status ->
            if (status is SessionStatus.SignedIn) status.user else null
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val mainGate = getMainGateUseCase.observeAsLoadStateFlow()
        .onEach {
            if (it is LoadState.Success && it.data != null) {
                gateId = it.data.id
            }
        }
        .stateIn(viewModelScope)
    val mainCctv = getCctvUseCase.observeMainAsLoadStateFlow().stateIn(viewModelScope)
    val mainParkingQuota = getMainParkingQuotaUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    val latestAccessLog = flow {
        emit(accessLogRepository.getLatestOpenLog())
    }
        .combine(mainGate) { logRes, gateState ->
            var latestLog: AccessLog? = null

            logRes
                .onSuccess { log ->
                    if (gateState is LoadState.Success && gateState.data != null)
                        latestLog = log
                }

            latestLog
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun openGate() {
        viewModelScope.launch {
            accessGateUseCase.open(gateId)
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

    fun closeGate() {
        viewModelScope.launch {
            accessGateUseCase.close(gateId)
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