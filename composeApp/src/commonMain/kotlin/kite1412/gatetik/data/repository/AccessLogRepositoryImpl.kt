package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.repository.AccessLogRepository
import kite1412.gatetik.domain.repository.AccessLogResult
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.network.domain.datasource.AccessLogRemoteDataSource

class AccessLogRepositoryImpl(
    private val remoteDataSource: AccessLogRemoteDataSource
) : AccessLogRepository {
    private val logTag = "AccessLogRepositoryImpl"

    override suspend fun getAll(): AccessLogResult<PaginatedListResult<AccessLog>> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get access logs"
    ) { throwError ->
        val res = remoteDataSource.getLogs(emptyMap())

        res ?: throwError()
    }

    override suspend fun getLatestOpenLog(): AccessLogResult<AccessLog?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get latest open log",
        action = remoteDataSource::getLatestOpenLog
    )
}