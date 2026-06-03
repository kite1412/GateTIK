package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.model.IotDevice

interface IotDeviceRemoteDataSource {
    suspend fun getByGateId(gateId: Int): IotDevice?
}