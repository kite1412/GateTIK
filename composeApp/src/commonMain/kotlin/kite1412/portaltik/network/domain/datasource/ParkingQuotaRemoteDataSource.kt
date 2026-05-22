package kite1412.portaltik.network.domain.datasource

import kite1412.portaltik.model.ParkingQuota

interface ParkingQuotaRemoteDataSource {
    suspend fun getMainParkingQuota(): ParkingQuota?
}