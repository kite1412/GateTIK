package kite1412.gatetik.domain.repository

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias UserResult<T> = Result<T, Error>

interface UserRepository {
    suspend fun getAll(role: UserRole? = null): UserResult<PaginatedListResult<User>>
}