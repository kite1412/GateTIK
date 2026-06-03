package kite1412.gatetik.network.mock.datasource

import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.network.domain.datasource.AccessLogRemoteDataSource
import kite1412.gatetik.network.mock.mockAccessLogs
import kotlinx.coroutines.delay

class MockAccessLogRemoteDataSource : AccessLogRemoteDataSource {
    override suspend fun getLogs(params: Map<String, String>): List<AccessLog> {
        delay(2000)
        return mockAccessLogs
    }

    override suspend fun getLatestOpenLog(): AccessLog = mockAccessLogs.first()
}