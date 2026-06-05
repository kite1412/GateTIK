package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.model.User

interface UserRemoteDataSource {
    suspend fun getAll(params: Map<String, String>): PaginatedListResult<User>?
}