package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.Gate
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource
import kite1412.portaltik.network.mock.mockGate
import kotlinx.coroutines.delay

class MockGateRemoteDataSource : GateRemoteDataSource {
    override suspend fun getGates(): List<Gate> = listOf(mockGate)

    override suspend fun getMainGate(): Gate {
        delay(2000)
        return mockGate
    }
}