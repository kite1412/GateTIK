package kite1412.gatetik.network.mock.datasource

import kite1412.gatetik.model.Cctv
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource
import kite1412.gatetik.network.mock.mockCctv
import kotlinx.coroutines.delay

class MockCctvRemoteDataSource : CctvRemoteDataSource {
    override suspend fun getMainCctv(): Cctv {
        delay(2000)
        return mockCctv
    }
}