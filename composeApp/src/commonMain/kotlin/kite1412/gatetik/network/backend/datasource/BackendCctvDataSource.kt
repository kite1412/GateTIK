package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendCctv
import kite1412.gatetik.network.backend.dto.request.BackendCctvCreateRequest
import kite1412.gatetik.network.backend.dto.request.BackendCctvUpdateRequest
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.backend.extension.toCreateRequest
import kite1412.gatetik.network.backend.extension.toUpdateRequest
import kite1412.gatetik.network.backend.util.BackendException
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource

class BackendCctvDataSource : CctvRemoteDataSource {
    override suspend fun getMainCctv(): Cctv? = BackendClient
        .get<BackendResponse<BackendCctv>>("cctv/main")
        .data
        ?.toModel()

    override suspend fun getAll(): List<Cctv> = BackendClient
        .get<BackendResponse<List<BackendCctv>>>("cctv")
        .data
        ?.map(BackendCctv::toModel)
        ?: emptyList()

    override suspend fun addCctv(data: CctvCreate): Cctv = BackendClient
        .post<BackendCctvCreateRequest, BackendResponse<BackendCctv>>(
            path = "cctv",
            body = data.toCreateRequest()
        )
        .data
        ?.toModel()
        ?: throw BackendException("Failed to create cctv")

    override suspend fun updateCctv(data: CctvUpdate): Cctv = BackendClient
        .patch<BackendCctvUpdateRequest, BackendResponse<BackendCctv>>(
            path = "cctv/${data.id}",
            body = data.toUpdateRequest()
        )
        .data
        ?.toModel()
        ?: throw BackendException("Failed to update cctv")

    override suspend fun deleteCctv(id: Int): Boolean = BackendClient
        .delete<BackendResponse<Unit>>("cctv/$id")
        .success
}