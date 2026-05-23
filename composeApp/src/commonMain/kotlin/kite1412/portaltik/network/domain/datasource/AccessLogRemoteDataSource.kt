package kite1412.portaltik.network.domain.datasource

import kite1412.portaltik.model.AccessLog

interface AccessLogRemoteDataSource {
    suspend fun getLatestLogs(): List<AccessLog>
}