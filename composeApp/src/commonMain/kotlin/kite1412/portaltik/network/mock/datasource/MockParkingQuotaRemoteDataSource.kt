package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.network.domain.datasource.ParkingQuotaRemoteDataSource
import kite1412.portaltik.network.mock.mockParkingQuota
import kotlinx.coroutines.delay

class MockParkingQuotaRemoteDataSource : ParkingQuotaRemoteDataSource {
    override suspend fun getMainParkingQuota(): ParkingQuota {
        delay(2000)
        return mockParkingQuota
    }
}