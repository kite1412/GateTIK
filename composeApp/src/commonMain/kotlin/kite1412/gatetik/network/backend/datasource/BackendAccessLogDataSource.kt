package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendAccessLog
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.domain.datasource.AccessLogRemoteDataSource

class BackendAccessLogDataSource : AccessLogRemoteDataSource {
    override suspend fun getLogs(params: Map<String, String>): List<AccessLog> =
        BackendClient.get<BackendResponse<List<BackendAccessLog>>>(
            path = "access-logs",
            params = params
        )
            .data
            ?.map(BackendAccessLog::toModel)
            ?: emptyList()

    override suspend fun getLatestOpenLog(): AccessLog? =
        BackendClient.get<BackendResponse<BackendAccessLog>>(
            path = "access-logs/last-opened"
        )
            .data
            ?.toModel()
}