package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.repository.ParkingQuotaRepository
import kite1412.gatetik.domain.repository.ParkingQuotaResult
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.network.domain.datasource.ParkingQuotaRemoteDataSource
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.Success
import kite1412.gatetik.util.Unknown
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ParkingQuotaRepositoryImpl(
    private val remoteDataSource: ParkingQuotaRemoteDataSource,
) : ParkingQuotaRepository {
    private val parkingQuota = MutableStateFlow<ParkingQuotaResult<ParkingQuota?>>(Result.Loading)
    private val logTag = "ParkingQuotaRepositoryImpl"

    override fun observeMainParkingQuota(): Flow<ParkingQuotaResult<ParkingQuota?>> = flow {
        getMainParkingQuota()
            .onSuccess {
                parkingQuota.value = Success(it)
            }
            .onError {
                parkingQuota.value = Error(Unknown())
            }

        emitAll(parkingQuota)
    }

    private suspend fun getMainParkingQuota(): ParkingQuotaResult<ParkingQuota?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main parking quota"
    ) { _ ->
        val parkingQuota = remoteDataSource.getMainParkingQuota()
        parkingQuota?.let {
            updateParkingQuotaFlow { parkingQuota }
        }

        parkingQuota
    }

    fun updateParkingQuotaFlow(block: (old: ParkingQuota?) -> ParkingQuota?) {
        this.parkingQuota.value = Success(
            block(
                if (parkingQuota.value is Result.Success)
                    (parkingQuota.value as Result.Success<ParkingQuota?>).data
                else null
            )
        )
    }
}