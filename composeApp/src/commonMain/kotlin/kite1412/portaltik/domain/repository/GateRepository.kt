package kite1412.portaltik.domain.repository

import kite1412.portaltik.model.Gate
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result

typealias GateResult<T> = Result<T, Error>

interface GateRepository {
    suspend fun getGates(): GateResult<List<Gate>>

    suspend fun getMainGate(): GateResult<Gate?>
}