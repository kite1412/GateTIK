package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendUser
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.domain.datasource.UserRemoteDataSource

class BackendUserDataSource : UserRemoteDataSource {
    override suspend fun getAll(role: UserRole?): PaginatedListResult<User>? =
        BackendClient.get<BackendResponse<List<BackendUser>>>(
            path = "users"
        )
            .toModel()

    private fun BackendResponse<List<BackendUser>>.toModel(): PaginatedListResult<User>? =
        if (data == null || pagination == null) null
        else PaginatedListResult(
            data = data.map(BackendUser::toModel),
            pagination = pagination.toModel()
        )
}