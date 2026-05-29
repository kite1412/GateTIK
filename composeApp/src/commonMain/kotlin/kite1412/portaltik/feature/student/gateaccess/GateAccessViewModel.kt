package kite1412.portaltik.feature.student.gateaccess

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.LocationPermissionController
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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GateAccessViewModel(
    authentication: Authentication,
    getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    locationPermissionController: LocationPermissionController,
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
        locationPermissionController.isPermissionGranted()
    )
        private set

    fun openGate() {
        viewModelScope.launch {
            getMainGateUseCase.observeAsFlow().first()?.let { gate ->
                openGateUseCase(gate.id)
            }
        }
    }
}