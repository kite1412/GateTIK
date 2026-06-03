package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.Location
import kite1412.gatetik.model.Gate

interface GateRemoteDataSource {
    suspend fun getMainGate(): Gate?

    suspend fun openGate(id: Int): Boolean

    suspend fun closeGate(id: Int): Boolean

    suspend fun enterGate(id: Int, location: Location): Boolean

    suspend fun exitGate(id: Int, location: Location): Boolean
}