package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.ParkingQuotaRepository
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMainParkingQuotaUseCase(private val parkingQuotaRepository: ParkingQuotaRepository) {
    fun observeAsLoadStateFlow(): Flow<LoadState<ParkingQuota?>> = flow {
        emit(LoadState.Loading("Memuat informasi parkir"))

        parkingQuotaRepository
            .getMainParkingQuota()
            .onError {
                emit(LoadState.Error("Gagal memuat informasi kuota parkir."))
            }
            .onSuccess { parkingQuota ->
                emit(LoadState.Success(parkingQuota))
            }
    }
}