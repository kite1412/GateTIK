package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.model.ParkingQuota

interface ParkingQuotaRemoteDataSource {
    suspend fun getMainParkingQuota(): ParkingQuota?

    suspend fun updateMainParkingQuota(parkingQuota: ParkingQuota): ParkingQuota
}