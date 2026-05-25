package kite1412.portaltik.data.repository

import kite1412.portaltik.data.util.tryOrThrowUnknown
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.domain.repository.GateResult
import kite1412.portaltik.model.Gate
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Success
import kite1412.portaltik.util.Unknown

class GateRepositoryImpl(
    private val remoteDataSource: GateRemoteDataSource
) : GateRepository {
    private val logTag = "GateRepositoryImpl"

    override suspend fun getGates(): GateResult<List<Gate>> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get gates.",
        action = remoteDataSource::getGates
    )

    override suspend fun getMainGate(): GateResult<Gate?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main gate",
        action = remoteDataSource::getMainGate
    )

    override suspend fun openGate(id: Int): GateResult<Boolean> {
        val res = remoteDataSource.openGate(id)

        return if (res) Success(res)
        else Error(Unknown())
    }
}