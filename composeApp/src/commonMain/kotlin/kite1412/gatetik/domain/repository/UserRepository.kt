package kite1412.gatetik.domain.repository

import kite1412.gatetik.common.extension.includeIfNotNull
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.model.UserCreate
import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias UserResult<T> = Result<T, Error>

interface UserRepository {
    suspend fun getAll(params: GetParams = GetParams()): UserResult<PaginatedListResult<User>>

    suspend fun addUser(data: UserCreate): UserResult<User>

    suspend fun updateUser(data: UserUpdate): UserResult<User>

    suspend fun deleteUser(id: Int): UserResult<Boolean>

    suspend fun previewKtm(studentId: Int): UserResult<ByteArray?>

    data class GetParams(
        val role: UserRole? = null,
        val status: UserStatus? = null,
        val search: String? = null,
        val perPage: Int? = null,
        val page: Int? = null
    ) {
        fun toMap() = mutableMapOf<String, String>().apply {
            includeIfNotNull("page", page?.toString())
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
    }
}