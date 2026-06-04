package kite1412.gatetik.network.mock.datasource

import kite1412.gatetik.Location
import kite1412.gatetik.domain.model.GateAccessType
import kite1412.gatetik.model.Gate
import kite1412.gatetik.network.domain.datasource.GateRemoteDataSource
import kite1412.gatetik.network.mock.mockGate
import kotlinx.coroutines.delay

class MockGateRemoteDataSource : GateRemoteDataSource {
    override suspend fun getMainGate(): Gate {
        delay(2000)
        return mockGate
    }
    override suspend fun updateMainGate(gate: Gate): Gate = gate
    override suspend fun openGate(id: Int): Boolean = true
    override suspend fun closeGate(id: Int): Boolean = true
    override suspend fun enterGate(id: Int, location: Location) = true
    override suspend fun exitGate(id: Int, location: Location): Boolean = true
    override suspend fun enterOrExitGate(
        id: Int,
        location: Location
    ): GateAccessType  = GateAccessType.ENTER
}