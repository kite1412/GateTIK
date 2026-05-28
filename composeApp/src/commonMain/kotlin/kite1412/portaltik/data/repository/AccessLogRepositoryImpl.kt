package kite1412.portaltik.data.repository

import kite1412.portaltik.Logger
import kite1412.portaltik.domain.repository.AccessLogRepository
import kite1412.portaltik.domain.repository.AccessLogResult
import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.network.domain.datasource.AccessLogRemoteDataSource
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Success
import kite1412.portaltik.util.Unknown

class AccessLogRepositoryImpl(
    private val remoteDataSource: AccessLogRemoteDataSource
) : AccessLogRepository {
    private val logTag = "AccessLogRepositoryImpl"

    override suspend fun getLatestOpenLog(): AccessLogResult<AccessLog?> = try {
        val res = remoteDataSource.getLatestOpenLog()

        Success(res)
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to get latest open log",
            throwable = e
        )
        Error(Unknown())
    }
}