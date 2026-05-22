package kite1412.portaltik.feature.admin.mobile.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.repository.CctvRepository
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.domain.repository.IotDeviceRepository
import kite1412.portaltik.domain.repository.ParkingQuotaRepository
import kite1412.portaltik.model.Cctv
import kite1412.portaltik.model.Gate
import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MobileAdminHomeViewModel(
    authentication: Authentication,
    private val cctvRepository: CctvRepository,
    private val gateRepository: GateRepository,
    private val iotDeviceRepository: IotDeviceRepository,
    private val parkinQuotaRepository: ParkingQuotaRepository
) : ViewModel() {
    val signedInUser = authentication.signedInUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    var mainGate by mutableStateOf<LoadState<Gate?>>(LoadState.Loading())
        private set
    var mainCctv by mutableStateOf<LoadState<Cctv?>>(LoadState.Loading())
        private set
    var mainParkingQuota by mutableStateOf<LoadState<ParkingQuota?>>(LoadState.Loading())
        private set
    var mainIotDevice by mutableStateOf<LoadState<IotDevice?>>(LoadState.Loading())
        private set

    init {
        viewModelScope.launch {
            launch { getMainGate() }
            launch { getMainCctv() }
            launch { getMainParkingQuota() }
        }
    }

    private suspend fun getMainGate() {
        gateRepository.getMainGate()
            .onError {
                this@MobileAdminHomeViewModel.mainGate = LoadState.Error("Gagal memuat informasi main gate.")
            }
            .onSuccess { gate ->
                this@MobileAdminHomeViewModel.mainGate = LoadState.Success(gate)
                gate?.let {
                    iotDeviceRepository.getByGateId(it.id)
                        .onError {
                            this@MobileAdminHomeViewModel.mainIotDevice = LoadState.Error("Gagal memuat informasi.")
                        }
                        .onSuccess { iotDevice ->
                            this@MobileAdminHomeViewModel.mainIotDevice = LoadState.Success(iotDevice)
                        }
                }
            }
    }

    private suspend fun getMainCctv() {
        cctvRepository
            .getMainCctv()
            .onError {
                this@MobileAdminHomeViewModel.mainCctv = LoadState.Error("Gagal memuat informasi cctv.")
            }
            .onSuccess { cctv ->
                this@MobileAdminHomeViewModel.mainCctv = LoadState.Success(cctv)
            }
    }

    private suspend fun getMainParkingQuota() {
        parkinQuotaRepository
            .getMainParkingQuota()
            .onError {
                this@MobileAdminHomeViewModel.mainParkingQuota = LoadState.Error("Gagal memuat informasi kuota parkir.")
            }
            .onSuccess { parkingQuota ->
                this@MobileAdminHomeViewModel.mainParkingQuota = LoadState.Success(parkingQuota)
            }
    }
}