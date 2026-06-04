package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendParkingQuota
import kite1412.gatetik.network.backend.dto.request.BackendParkingQuotaUpdateRequest
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.backend.extension.toUpdateRequest
import kite1412.gatetik.network.backend.util.BackendException
import kite1412.gatetik.network.domain.datasource.ParkingQuotaRemoteDataSource

class BackendParkingQuotaDataSource : ParkingQuotaRemoteDataSource {
    override suspend fun getMainParkingQuota(): ParkingQuota? =
        BackendClient.get<BackendResponse<BackendParkingQuota>>(
            path = "parking-quota"
        )
            .data
            ?.toModel()

    override suspend fun updateMainParkingQuota(parkingQuota: ParkingQuota): ParkingQuota =
        BackendClient.patch<BackendParkingQuotaUpdateRequest, BackendResponse<BackendParkingQuota>>(
            path = "parking-quota",
            body = parkingQuota.toUpdateRequest()
        )
            .data
            ?.toModel()
            ?: throw BackendException("Failed to update main parking quota")
}