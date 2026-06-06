package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.domain.model.PaginatedListResult
import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.model.User
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendUser
import kite1412.gatetik.network.backend.dto.request.BackendUserUpdateRequest
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.backend.extension.toUpdateRequest
import kite1412.gatetik.network.backend.util.BackendException
import kite1412.gatetik.network.domain.datasource.UserRemoteDataSource

class BackendUserDataSource : UserRemoteDataSource {
    override suspend fun getAll(params: Map<String, String>): PaginatedListResult<User>? =
        BackendClient.get<BackendResponse<List<BackendUser>>>(
            path = "users",
            params = params
        )
            .toModel()

    override suspend fun updateUser(data: UserUpdate): User =
        BackendClient.patch<BackendUserUpdateRequest, BackendResponse<BackendUser>>(
            path = "users/${data.id}",
            body = data.toUpdateRequest()
        )
            .data
            ?.toModel()
            ?: throw BackendException("Failed to update user: ${data.id}")

    override suspend fun deleteUser(id: Int): Boolean =
        BackendClient.delete<BackendResponse<Unit>>(path = "users/${id}")
            .success


    private fun BackendResponse<List<BackendUser>>.toModel(): PaginatedListResult<User>? =
        if (data == null || pagination == null) null
        else PaginatedListResult(
            data = data.map(BackendUser::toModel),
            pagination = pagination.toModel()
        )
}