package kite1412.portaltik.network.domain.datasource

import kite1412.portaltik.model.Gate

interface GateRemoteDataSource {
    suspend fun getGates(): List<Gate>

    suspend fun getMainGate(): Gate?

    suspend fun openGate(id: Int): Boolean

    suspend fun closeGate(id: Int): Boolean
}