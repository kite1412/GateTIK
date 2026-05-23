package kite1412.portaltik.domain.usecase

import kite1412.portaltik.domain.repository.IotDeviceRepository
import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMainIotDeviceUseCase(private val iotDeviceRepository: IotDeviceRepository) {
    operator fun invoke(mainGateId: Int): Flow<LoadState<IotDevice?>> = flow {
        emit(LoadState.Loading())

        iotDeviceRepository.getByGateId(mainGateId)
            .onError {
                emit(LoadState.Error("Gagal memuat informasi."))
            }
            .onSuccess { iotDevice ->
                emit(LoadState.Success(iotDevice))
            }
    }
}