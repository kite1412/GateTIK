package kite1412.portaltik.domain.repository

import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result

typealias AccessLogResult<T> = Result<T, Error>

interface AccessLogRepository {
    suspend fun getLatestLogs(): AccessLogResult<List<AccessLog>>
}