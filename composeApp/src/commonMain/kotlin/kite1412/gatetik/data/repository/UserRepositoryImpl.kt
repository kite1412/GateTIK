package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.domain.repository.UserRepository
import kite1412.gatetik.domain.repository.UserResult
import kite1412.gatetik.model.User
import kite1412.gatetik.network.domain.datasource.UserRemoteDataSource

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {
    private val logTag = "UserRepository"

    override suspend fun getAll(
        params: UserRepository.GetParams
    ): UserResult<PaginatedListResult<User>> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get users"
    ) { throwError ->
        val res = remoteDataSource.getAll(params.toMap())

        res ?: throwError()
    }

    override suspend fun updateUser(data: UserUpdate): UserResult<User> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to update user: ${data.id}"
    ) { _ -> remoteDataSource.updateUser(data) }

    override suspend fun deleteUser(id: Int): UserResult<Boolean> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to update user: $id"
    ) { _ -> remoteDataSource.deleteUser(id) }

    override suspend fun previewKtm(studentId: Int): UserResult<ByteArray?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get ktm preview"
    ) { _ -> remoteDataSource.previewKtm(studentId) }
}