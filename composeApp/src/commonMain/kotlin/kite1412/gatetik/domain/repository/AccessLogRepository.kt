package kite1412.gatetik.domain.repository

import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias AccessLogResult<T> = Result<T, Error>

interface AccessLogRepository {
    suspend fun getLatestOpenLog(): AccessLogResult<AccessLog?>
}