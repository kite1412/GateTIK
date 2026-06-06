package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.model.UserCreate
import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.model.User

interface UserRemoteDataSource {
    suspend fun getAll(params: Map<String, String>): PaginatedListResult<User>?

    suspend fun addUser(data: UserCreate): User

    suspend fun updateUser(data: UserUpdate): User

    suspend fun deleteUser(id: Int): Boolean

    suspend fun previewKtm(studentId: Int): ByteArray?
}