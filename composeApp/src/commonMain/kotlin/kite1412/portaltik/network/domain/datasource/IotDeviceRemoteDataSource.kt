package kite1412.portaltik.network.domain.datasource

import kite1412.portaltik.model.IotDevice

interface IotDeviceRemoteDataSource {
    suspend fun getByGateId(gateId: Int): IotDevice?
}