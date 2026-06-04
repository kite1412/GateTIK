package kite1412.gatetik.network.mock.datasource

import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.network.domain.datasource.ParkingQuotaRemoteDataSource
import kite1412.gatetik.network.mock.mockParkingQuota
import kotlinx.coroutines.delay

class MockParkingQuotaRemoteDataSource : ParkingQuotaRemoteDataSource {
    override suspend fun getMainParkingQuota(): ParkingQuota {
        delay(2000)
        return mockParkingQuota
    }
    override suspend fun updateMainParkingQuota(parkingQuota: ParkingQuota): ParkingQuota = parkingQuota
}