package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.ParkingQuotaRepository
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetMainParkingQuotaUseCase(private val parkingQuotaRepository: ParkingQuotaRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeAsLoadStateFlow(): Flow<LoadState<ParkingQuota?>> = parkingQuotaRepository
        .observeMainParkingQuota()
        .mapLatest { res ->
            when (res) {
                is Result.Error -> LoadState.Error("Gagal memuat informasi kuota parkir")
                is Result.Loading -> LoadState.Loading("Memuat informasi parkir")
                is Result.Success -> LoadState.Success(res.data)
            }
        }
}