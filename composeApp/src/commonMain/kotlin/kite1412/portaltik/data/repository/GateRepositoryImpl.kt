package kite1412.portaltik.data.repository

import kite1412.portaltik.data.util.tryOrThrowUnknown
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.domain.repository.GateResult
import kite1412.portaltik.model.Gate
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource

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
}