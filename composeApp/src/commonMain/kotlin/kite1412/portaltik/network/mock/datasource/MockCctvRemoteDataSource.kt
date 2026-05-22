package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.Cctv
import kite1412.portaltik.network.domain.datasource.CctvRemoteDataSource
import kite1412.portaltik.network.mock.mockCctv

class MockCctvRemoteDataSource : CctvRemoteDataSource {
    override suspend fun getCctvs(): List<Cctv> = listOf(mockCctv)

    override suspend fun getMainCctv(): Cctv = mockCctv
}