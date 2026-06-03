package kite1412.gatetik.feature.student.gateaccess

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.LocationPermissionController
import kite1412.gatetik.LocationService
import kite1412.gatetik.LocationState
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.domain.usecase.GetMainGateUseCase
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.domain.usecase.OpenGateUseCase
import kite1412.gatetik.model.User
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.stateIn
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GateAccessViewModel(
    authentication: Authentication,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    locationService: LocationService,
    private val getMainGateUseCase: GetMainGateUseCase,
    private val openGateUseCase: OpenGateUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: StateFlow<User?> = authentication.sessionStatus
        .mapLatest { state ->
            if (state is SessionStatus.SignedIn) state.user
            else null
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    val parkingQuota = getMainParkingQuotaUseCase().stateIn(viewModelScope)
    var isLocationPermissionGranted by mutableStateOf(
        LocationPermissionController.isPermissionGranted()
    )
        private set
    var delayAction by mutableStateOf(false)
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationState = snapshotFlow { isLocationPermissionGranted }
        .flatMapLatest { granted ->
            if (granted) locationService
                .observeLocationState()
            else flowOf(LocationState.Loading)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LocationState.Loading
        )

    fun openGate() {
        viewModelScope.launch {
            getMainGateUseCase.observeAsFlow().first()?.let { gate ->
                val state = locationState.first()

                if (state is LocationState.Available) {
                    openGateUseCase.enter(
                        id = gate.id,
                        location = state.currentLocation
                    )
                        .onSuccess { success ->
                            _uiEvent.emit(
                                UiEvent.ShowSnackbar(if (success) "Berhasil membuka gate" else "Gagal membuka gate, pastikan berada di sekitar gate")
                            )
                            if (success) {
                                delayAction = true
                                delay(5000)
                                delayAction = false
                            }
                        }
                        .onError {
                            _uiEvent.emit(
                                UiEvent.ShowSnackbar("Gagal membuka gate, pastikan berada di sekitar gate")
                            )
                        }
                }
            }
        }
    }

    fun updateIsLocationPermissionGranted(value: Boolean) {
        isLocationPermissionGranted = value
    }
}