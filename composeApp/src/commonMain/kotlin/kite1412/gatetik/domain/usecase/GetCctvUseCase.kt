package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.CctvRepository
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCctvUseCase(private val cctvRepository: CctvRepository) {
    fun observeMainAsLoadStateFlow(): Flow<LoadState<Cctv?>> = flow {
        emit(LoadState.Loading("Memuat CCTV"))

        cctvRepository
            .getMainCctv()
            .onError {
                emit(LoadState.Error("Gagal memuat informasi cctv."))
            }
            .onSuccess { cctv ->
                emit(LoadState.Success(cctv))
            }
    }

    fun observeAllAsLoadStateFlow(): Flow<LoadState<List<Cctv>>> = flow {
        emit(LoadState.Loading("Memuat CCTV"))

        cctvRepository
            .getAll()
            .onSuccess {
                emit(LoadState.Success(it))
            }
            .onError {
                emit(LoadState.Error("Gagal memuat informasi cctv."))
            }
    }
}