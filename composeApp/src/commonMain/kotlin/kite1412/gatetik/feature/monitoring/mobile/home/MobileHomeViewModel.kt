package kite1412.gatetik.feature.monitoring.mobile.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.Gate
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.showSnackbar
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class MobileHomeViewModel(
    authentication: Authentication,
    private val getMainGateUseCase: GetMainGateUseCase,
    private val getCctvUseCase: GetCctvUseCase,
    private val getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
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

    private val _mainGate = MutableStateFlow<LoadState<Gate?>>(LoadState.Loading())
    val mainGate = _mainGate.asStateFlow()

    private val _cctvs = MutableStateFlow<LoadState<List<Cctv>>>(LoadState.Loading())
    val cctvs = _cctvs.asStateFlow()

    private val _mainParkingQuota = MutableStateFlow<LoadState<ParkingQuota?>>(LoadState.Loading())
    val mainParkingQuota = _mainParkingQuota.asStateFlow()

    init {
        getMainGateUseCase.observeAsLoadStateFlow()
            .onEach {
                _mainGate.value = it
                if (it is LoadState.Success && it.data != null) {
                    gateId = it.data.id
                }
            }
            .launchIn(viewModelScope)

        getCctvUseCase.observeAllAsLoadStateFlow()
            .onEach { _cctvs.value = it }
            .launchIn(viewModelScope)

        getMainParkingQuotaUseCase.observeAsLoadStateFlow()
            .onEach { _mainParkingQuota.value = it }
            .launchIn(viewModelScope)
    }

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
    var isRefreshing by mutableStateOf(false)
        private set

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

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            var isError = false

            val gateJob = launch {
                val res = getMainGateUseCase.observeAsLoadStateFlow().first { it !is LoadState.Loading }
                if (res is LoadState.Success) _mainGate.value = res
                else if (res is LoadState.Error) isError = true
            }
            val cctvsJob = launch {
                val res = getCctvUseCase.observeAllAsLoadStateFlow().first { it !is LoadState.Loading }
                if (res is LoadState.Success) _cctvs.value = res
                else if (res is LoadState.Error) isError = true
            }
            val parkingJob = launch {
                val res = getMainParkingQuotaUseCase.observeAsLoadStateFlow().first { it !is LoadState.Loading }
                if (res is LoadState.Success) _mainParkingQuota.value = res
                else if (res is LoadState.Error) isError = true
            }

            joinAll(gateJob, cctvsJob, parkingJob)
            if (isError) _uiEvent.showSnackbar("Gagal memuat ulang")
            isRefreshing = false
        }
    }
}