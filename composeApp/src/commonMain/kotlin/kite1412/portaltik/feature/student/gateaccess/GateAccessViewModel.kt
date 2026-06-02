package kite1412.portaltik.feature.student.gateaccess

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.LocationPermissionController
import kite1412.portaltik.LocationService
import kite1412.portaltik.LocationState
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.SessionStatus
import kite1412.portaltik.domain.usecase.GetMainGateUseCase
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.domain.usecase.OpenGateUseCase
import kite1412.portaltik.model.User
import kite1412.portaltik.ui.util.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
                }
            }
        }
    }

    fun updateIsLocationPermissionGranted(value: Boolean) {
        isLocationPermissionGranted = value
    }
}