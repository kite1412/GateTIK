package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.repository.ParkingQuotaRepository
import kite1412.gatetik.domain.repository.ParkingQuotaResult
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.network.domain.datasource.ParkingQuotaRemoteDataSource

class ParkingQuotaRepositoryImpl(
    private val remoteDataSource: ParkingQuotaRemoteDataSource
) : ParkingQuotaRepository {
    private val logTag = "ParkingQuotaRepositoryImpl"

    override suspend fun getMainParkingQuota(): ParkingQuotaResult<ParkingQuota?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main parking quota",
        action = remoteDataSource::getMainParkingQuota
    )
}