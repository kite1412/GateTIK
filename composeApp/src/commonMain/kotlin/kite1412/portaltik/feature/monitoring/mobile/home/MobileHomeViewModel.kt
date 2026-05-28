package kite1412.portaltik.feature.monitoring.mobile.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.SessionStatus
import kite1412.portaltik.domain.repository.AccessLogRepository
import kite1412.portaltik.domain.usecase.CloseGateUseCase
import kite1412.portaltik.domain.usecase.GetMainCctvUseCase
import kite1412.portaltik.domain.usecase.GetMainGateUseCase
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.domain.usecase.OpenGateUseCase
import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.UiEvent
import kite1412.portaltik.ui.util.stateIn
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
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
    getMainCctvUseCase: GetMainCctvUseCase,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    accessLogRepository: AccessLogRepository,
    private val openGateUseCase: OpenGateUseCase,
    private val closeGateUseCase: CloseGateUseCase
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
    val mainGate = getMainGateUseCase()
        .onEach {
            if (it is LoadState.Success && it.data != null) {
                gateId = it.data.id
            }
        }
        .stateIn(viewModelScope)
    val mainCctv = getMainCctvUseCase().stateIn(viewModelScope)
    val mainParkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)
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
            openGateUseCase(gateId)
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

    fun closeGate() {
        viewModelScope.launch {
            closeGateUseCase(gateId)
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