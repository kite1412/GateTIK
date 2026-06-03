package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.GateRepository
import kite1412.gatetik.model.Gate
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMainGateUseCase(private val gateRepository: GateRepository) {
    fun observeAsFlow(): Flow<Gate?> = flow {
        gateRepository.getMainGate()
            .onError {
                emit(null)
            }
            .onSuccess { gate ->
                emit(gate)
            }
    }

    fun observeAsLoadStateFlow(): Flow<LoadState<Gate?>> = flow {
        emit(LoadState.Loading())

        gateRepository.getMainGate()
            .onError {
                emit(LoadState.Error("Gagal memuat informasi main gate."))
            }
            .onSuccess { gate ->
                emit(LoadState.Success(gate))
            }
    }
}