package kite1412.gatetik.network.mock.datasource

import kite1412.gatetik.model.IotDevice
import kite1412.gatetik.network.domain.datasource.IotDeviceRemoteDataSource
import kite1412.gatetik.network.mock.mockIotDevice
import kotlinx.coroutines.delay

class MockIotDeviceRemoteDataSource : IotDeviceRemoteDataSource {
    override suspend fun getByGateId(gateId: Int): IotDevice {
        delay(3000)
        return mockIotDevice
    }
}