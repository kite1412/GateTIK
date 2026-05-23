package kite1412.portaltik.data.repository

import kite1412.portaltik.data.util.tryOrThrowUnknown
import kite1412.portaltik.domain.repository.AccessLogRepository
import kite1412.portaltik.domain.repository.AccessLogResult
import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.network.domain.datasource.AccessLogRemoteDataSource

class AccessLogRepositoryImpl(
    private val remoteDataSource: AccessLogRemoteDataSource
) : AccessLogRepository {
    private val logTag = "AccessLogRepositoryImpl"

    override suspend fun getLatestLogs(): AccessLogResult<List<AccessLog>> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get latest access logs",
        action = remoteDataSource::getLatestLogs
    )
}