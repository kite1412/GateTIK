package kite1412.portaltik.data.repository

import kite1412.portaltik.data.util.tryOrThrowUnknown
import kite1412.portaltik.domain.repository.ParkingQuotaRepository
import kite1412.portaltik.domain.repository.ParkingQuotaResult
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.network.domain.datasource.ParkingQuotaRemoteDataSource

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