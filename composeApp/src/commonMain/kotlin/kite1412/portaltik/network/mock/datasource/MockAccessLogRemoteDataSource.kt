package kite1412.portaltik.network.mock.datasource

import kite1412.portaltik.model.AccessLog
import kite1412.portaltik.network.domain.datasource.AccessLogRemoteDataSource
import kite1412.portaltik.network.mock.mockAccessLogs
import kotlinx.coroutines.delay

class MockAccessLogRemoteDataSource : AccessLogRemoteDataSource {
    override suspend fun getLogs(params: Map<String, String>): List<AccessLog> {
        delay(2000)
        return mockAccessLogs
    }

    override suspend fun getLatestOpenLog(): AccessLog = mockAccessLogs.first()
}