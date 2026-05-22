package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.network.domain.datasource.IotDeviceRemoteDataSource
import kite1412.portaltik.network.mock.mockIotDevice
import kotlinx.coroutines.delay

class MockIotDeviceRemoteDataSource : IotDeviceRemoteDataSource {
    override suspend fun getByGateId(gateId: Int): IotDevice {
        delay(3000)
        return mockIotDevice
    }
}