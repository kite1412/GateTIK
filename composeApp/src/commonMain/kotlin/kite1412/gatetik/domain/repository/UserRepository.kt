package kite1412.gatetik.domain.repository

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias UserResult<T> = Result<T, Error>

interface UserRepository {
    suspend fun getAll(params: GetParams = GetParams()): UserResult<PaginatedListResult<User>>

    data class GetParams(
        val role: UserRole? = null,
        val status: UserStatus? = null,
        val search: String? = null,
        val perPage: Int? = null
    ) {
        fun toMap() = mutableMapOf<String, String>().apply {
            includeIfNotNull(
                key = "role",
                value = role?.toIdString()?.lowercase()
            )
            includeIfNotNull(
                key = "status",
                value = status?.name?.lowercase()
            )
            includeIfNotNull(
                key = "search",
                value = search?.takeIf { it.isNotBlank() }
            )
            includeIfNotNull("per_page", perPage?.toString())
        }

        private fun MutableMap<String, String>.includeIfNotNull(key: String, value: String?) {
            if (value != null) set(key, value)
        }
    }
}