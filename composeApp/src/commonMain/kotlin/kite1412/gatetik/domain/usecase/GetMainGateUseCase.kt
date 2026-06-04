package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.GateRepository
import kite1412.gatetik.model.Gate
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeAsLoadStateFlow(): Flow<LoadState<Gate?>> = gateRepository
        .observeMainGate()
        .mapLatest { res ->
            when (res) {
                is Result.Error -> LoadState.Error("Gagal memuat informasi main gate")
                is Result.Loading -> LoadState.Loading("Memuat informasi main gate")
                is Result.Success -> LoadState.Success(res.data)
            }
        }
}