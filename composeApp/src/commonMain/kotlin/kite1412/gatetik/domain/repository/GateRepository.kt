package kite1412.gatetik.domain.repository

import kite1412.gatetik.Location
import kite1412.gatetik.domain.model.GateAccessType
import kite1412.gatetik.model.Gate
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias GateResult<T> = Result<T, Error>

interface GateRepository {
    suspend fun getMainGate(): GateResult<Gate?>

    suspend fun openGate(id: Int): GateResult<Boolean>

    suspend fun closeGate(id: Int): GateResult<Boolean>

    suspend fun enterGate(id: Int, location: Location): GateResult<Boolean>

    suspend fun exitGate(id: Int, location: Location): GateResult<Boolean>

    suspend fun enterOrExitGate(id: Int, location: Location): GateResult<GateAccessType>
}