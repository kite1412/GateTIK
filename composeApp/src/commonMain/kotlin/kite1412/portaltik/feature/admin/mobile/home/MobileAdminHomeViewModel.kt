package kite1412.portaltik.feature.admin.mobile.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.usecase.CloseGateUseCase
import kite1412.portaltik.domain.usecase.GetIotDeviceUseCase
import kite1412.portaltik.domain.usecase.GetMainCctvUseCase
import kite1412.portaltik.domain.usecase.GetMainGateUseCase
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.domain.usecase.OpenGateUseCase
import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.UiEvent
import kite1412.portaltik.ui.util.stateIn
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MobileAdminHomeViewModel(
    authentication: Authentication,
    getMainGateUseCase: GetMainGateUseCase,
    getIotDeviceUseCase: GetIotDeviceUseCase,
    getMainCctvUseCase: GetMainCctvUseCase,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    private val openGateUseCase: OpenGateUseCase,
    private val closeGateUseCase: CloseGateUseCase
) : ViewModel() {
    private var gateId = 0
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val signedInUser = authentication.signedInUser
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
    @OptIn(ExperimentalCoroutinesApi::class)
    val mainIotDevice: StateFlow<LoadState<IotDevice?>> = getIotDeviceUseCase
        .fromGateLoadStateFlow(mainGate)
        .stateIn(viewModelScope)

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