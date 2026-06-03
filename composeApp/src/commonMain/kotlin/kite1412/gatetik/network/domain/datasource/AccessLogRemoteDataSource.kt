package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.model.AccessLog

interface AccessLogRemoteDataSource {
    suspend fun getLogs(params: Map<String, String> = emptyMap()): List<AccessLog>

    suspend fun getLatestOpenLog(): AccessLog?
}