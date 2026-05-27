package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.Cctv
import kite1412.portaltik.network.domain.datasource.CctvRemoteDataSource
import kite1412.portaltik.network.mock.mockCctv
import kotlinx.coroutines.delay

class MockCctvRemoteDataSource : CctvRemoteDataSource {
    override suspend fun getMainCctv(): Cctv {
        delay(2000)
        return mockCctv
    }
}