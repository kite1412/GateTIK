package kite1412.portaltik.data.repository

import kite1412.portaltik.data.util.tryOrThrowUnknown
import kite1412.portaltik.domain.repository.IotDeviceRepository
import kite1412.portaltik.domain.repository.IotDeviceResult
import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.network.domain.datasource.IotDeviceRemoteDataSource

class IotDeviceRepositoryImpl(
    private val remoteDataSource: IotDeviceRemoteDataSource
) : IotDeviceRepository {
    private val logTag = "IotDeviceRepositoryImpl"

    override suspend fun getByGateId(gateId: Int): IotDeviceResult<IotDevice?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get iot device by gate id: $gateId",
    ){ remoteDataSource.getByGateId(gateId) }
}