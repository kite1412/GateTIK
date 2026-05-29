package kite1412.portaltik.domain.repository

import kite1412.portaltik.Location
import kite1412.portaltik.model.Gate
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result

typealias GateResult<T> = Result<T, Error>

interface GateRepository {
    suspend fun getMainGate(): GateResult<Gate?>

    suspend fun openGate(id: Int): GateResult<Boolean>

    suspend fun closeGate(id: Int): GateResult<Boolean>

    suspend fun enterGate(id: Int, location: Location): GateResult<Boolean>

    suspend fun exitGate(id: Int, location: Location): GateResult<Boolean>
}