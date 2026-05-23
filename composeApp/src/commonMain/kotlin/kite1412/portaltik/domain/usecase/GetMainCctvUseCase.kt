package kite1412.portaltik.domain.usecase

import kite1412.portaltik.domain.repository.CctvRepository
import kite1412.portaltik.model.Cctv
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMainCctvUseCase(private val cctvRepository: CctvRepository) {
    operator fun invoke(): Flow<LoadState<Cctv?>> = flow {
        emit(LoadState.Loading())

        cctvRepository
            .getMainCctv()
            .onError {
                emit(LoadState.Error("Gagal memuat informasi cctv."))
            }
            .onSuccess { cctv ->
                emit(LoadState.Success(cctv))
            }
    }
}