package kite1412.gatetik.domain.repository

import kite1412.gatetik.common.extension.includeIfNotNull
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias AccessLogResult<T> = Result<T, Error>

interface AccessLogRepository {
    suspend fun getAll(params: GetParams = GetParams()): AccessLogResult<PaginatedListResult<AccessLog>>

    suspend fun getLatestOpenLog(): AccessLogResult<AccessLog?>

    data class GetParams(
        val status: AccessStatus? = null,
        val method: AccessMethod? = null,
        val action: AccessAction? = null,
        val search: String? = null,
        val page: Int? = null,
        val perPage: Int? = null,
        val isDescending: Boolean = true,
        val period: LogPeriod? = null
    ) {
        fun toMap() = mutableMapOf<String, String>().apply {
            includeIfNotNull(
                key = "access_status",
                value = status?.name?.lowercase()
            )
            includeIfNotNull(
                key = "access_method",
                value = method?.name?.lowercase()
            )
            includeIfNotNull(
                key = "action",
                value = action?.name?.lowercase()
            )
            includeIfNotNull(
                key = "search",
                value = search?.takeIf { it.isNotBlank() }
            )
            includeIfNotNull("page", page?.toString())
            includeIfNotNull("per_page", perPage?.toString())
            includeIfNotNull("sort_order", if (isDescending) "desc" else "asc")
            includeIfNotNull("period", period?.string)
        }
    }

    enum class LogPeriod(val string: String) {
        DAY("24h"), WEEK("7d"), MONTH("30d")
    }
}