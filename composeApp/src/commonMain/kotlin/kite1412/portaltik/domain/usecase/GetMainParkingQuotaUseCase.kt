package kite1412.portaltik.domain.usecase

import kite1412.portaltik.domain.repository.ParkingQuotaRepository
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMainParkingQuotaUseCase(private val parkingQuotaRepository: ParkingQuotaRepository) {
    operator fun invoke(): Flow<LoadState<ParkingQuota?>> = flow {
        emit(LoadState.Loading())

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