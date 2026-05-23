package kite1412.portaltik.domain.usecase

import kite1412.portaltik.domain.repository.IotDeviceRepository
import kite1412.portaltik.model.Gate
import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetIotDeviceUseCase(private val iotDeviceRepository: IotDeviceRepository) {
    operator fun invoke(gateId: Int): Flow<LoadState<IotDevice?>> = flow {
        emit(LoadState.Loading())

        iotDeviceRepository.getByGateId(gateId)
            .onError {
                emit(LoadState.Error("Gagal memuat informasi."))
            }
            .onSuccess { iotDevice ->
                emit(LoadState.Success(iotDevice))
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fromGateLoadStateFlow(flow: Flow<LoadState<Gate?>>): Flow<LoadState<IotDevice?>> =
        flow.flatMapLatest { state ->
            if (state is LoadState.Success && state.data != null)
                invoke(state.data.id)
            else flowOf(
                when (state) {
                    is LoadState.Error -> LoadState.Error("Gagal memuat informasi.")
                    is LoadState.Loading -> LoadState.Loading()
                    is LoadState.Success -> LoadState.Success(null)
                }
            )
        }
}