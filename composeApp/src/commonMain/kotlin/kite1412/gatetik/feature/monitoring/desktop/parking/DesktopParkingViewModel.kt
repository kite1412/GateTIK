package kite1412.gatetik.feature.monitoring.desktop.parking

import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.usecase.GetMainGateUseCase
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.domain.usecase.UpdateMainGateUseCase
import kite1412.gatetik.domain.usecase.UpdateMainParkingQuotaUseCase
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.ui.util.stateIn
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DesktopParkingViewModel(
    authentication: Authentication,
    dataStore: GateTikDataStore,
    getMainGateUseCase: GetMainGateUseCase,
    private val getMainParkingQuotaUseCase: GetMainParkingQuotaUseCase,
    private val updateMainGateUseCase: UpdateMainGateUseCase,
    private val updateMainParkingQuotaUseCase: UpdateMainParkingQuotaUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val mainParkingQuota = getMainParkingQuotaUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)
    val mainGate = getMainGateUseCase.observeAsLoadStateFlow().stateIn(viewModelScope)

    init {
        polling(::pollData)
    }

    fun refreshParkingQuota() {
        viewModelScope.launch {
            pollData()
            _uiEvent.emit(
                UiEvent.ShowSnackbar("Data dimuat ulang")
            )
        }
    }

    fun updateParkingCapacity(capacity: Int) {
        viewModelScope.launch {
            mainParkingQuota.value.data?.let { parkingQuota ->
                update(
                    newValue = parkingQuota.copy(totalSlots = capacity),
                    successMessage = "Berhasil memperbarui kapasitas parkir mahasiswa",
                    errorMessage = "Gagal memperbarui kapasitas parkir",
                    updater = updateMainParkingQuotaUseCase::invoke
                )
            }
        }
    }

    fun updateAutoRestrictStudent(autoRestrict: Boolean) {
        viewModelScope.launch {
            mainParkingQuota.value.data?.let { parkingQuota ->
                update(
                    newValue = parkingQuota.copy(autoRestrictStudents = autoRestrict),
                    successMessage = "Berhasil memperbarui pembatasan kuota parkir otomatis",
                    errorMessage = "Gagal memperbarui pembatasan kuota parkir otomatis",
                    updater = updateMainParkingQuotaUseCase::invoke
                )
            }
        }
    }

    fun updateAllowedRadiusMeter(radiusMeter: Int) {
        viewModelScope.launch {
            mainGate.value.data?.let { gate ->
                update(
                    newValue = gate.copy(allowedRadiusMeter = radiusMeter),
                    successMessage = "Berhasil memperbarui batas radius",
                    errorMessage = "Gagal memperbarui batas radius",
                    updater = updateMainGateUseCase::invoke
                )
            }
        }
    }

    private suspend fun pollData() =
        getMainParkingQuotaUseCase
            .observeAsResultFlow()
            .first { it !is Result.Loading }
            .toPollingResult("Gagal memperbarui data parkir")

    private suspend fun <T, E : Error> update(
        newValue: T,
        successMessage: String,
        errorMessage: String,
        updater: suspend (T) -> Result<T, E>
    ) {
        updater(newValue)
            .onSuccess {
                _uiEvent.emit(UiEvent.ShowSnackbar(successMessage))
            }
            .onError {
                _uiEvent.emit(UiEvent.ShowSnackbar(errorMessage))
            }
    }
}