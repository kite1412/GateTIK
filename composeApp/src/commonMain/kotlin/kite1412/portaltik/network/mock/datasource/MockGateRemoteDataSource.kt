package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.Gate
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource
import kite1412.portaltik.network.mock.mockGate
import kotlinx.coroutines.delay

class MockGateRemoteDataSource : GateRemoteDataSource {
    override suspend fun getMainGate(): Gate {
        delay(2000)
        return mockGate
    }

    override suspend fun openGate(id: Int): Boolean = true
    override suspend fun closeGate(id: Int): Boolean = true
}