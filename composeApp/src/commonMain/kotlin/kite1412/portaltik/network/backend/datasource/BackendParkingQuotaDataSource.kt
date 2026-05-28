package kite1412.portaltik.network.backend.datasource

import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.network.backend.BackendClient
import kite1412.portaltik.network.backend.dto.model.BackendParkingQuota
import kite1412.portaltik.network.backend.dto.response.BackendResponse
import kite1412.portaltik.network.domain.datasource.ParkingQuotaRemoteDataSource

class BackendParkingQuotaDataSource : ParkingQuotaRemoteDataSource {
    override suspend fun getMainParkingQuota(): ParkingQuota? =
        BackendClient.get<BackendResponse<BackendParkingQuota>>(
            path = "parking-quota"
        )
            .data
            ?.toModel()
}