package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.repository.UserRepository
import kite1412.gatetik.domain.repository.UserResult
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.network.domain.datasource.UserRemoteDataSource

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {
    private val logTag = "UserRepository"

    override suspend fun getAll(
        role: UserRole?
    ): UserResult<PaginatedListResult<User>> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get users"
    ) { throwError ->
        val res = remoteDataSource.getAll(role)

        res ?: throwError()
    }
}