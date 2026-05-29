package kite1412.portaltik.network.domain.datasource

import kite1412.portaltik.Location
import kite1412.portaltik.model.Gate

interface GateRemoteDataSource {
    suspend fun getMainGate(): Gate?

    suspend fun openGate(id: Int): Boolean

    suspend fun closeGate(id: Int): Boolean

    suspend fun enterGate(id: Int, location: Location): Boolean

    suspend fun exitGate(id: Int, location: Location): Boolean
}