package kite1412.gatetik.network.mock.datasource

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.model.Pagination
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.network.domain.datasource.AccessLogRemoteDataSource
import kite1412.gatetik.network.mock.mockAccessLogs
import kotlinx.coroutines.delay

class MockAccessLogRemoteDataSource : AccessLogRemoteDataSource {
    override suspend fun getLogs(params: Map<String, String>): PaginatedListResult<AccessLog> {
        delay(2000)
        return PaginatedListResult(
            data = mockAccessLogs,
            pagination = Pagination(
                total = -1,
                perPage = -1,
                currentPage = -1,
                lastPage = -1,
                from = -1,
                to = -1
            )
        )
    }

    override suspend fun getLatestOpenLog(): AccessLog = mockAccessLogs.first()
}