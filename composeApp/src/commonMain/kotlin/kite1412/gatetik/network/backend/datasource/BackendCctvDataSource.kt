package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.domain.repository.CctvRepository
import kite1412.gatetik.domain.repository.CctvResult
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendCctv
import kite1412.gatetik.network.backend.dto.request.BackendCctvCreateRequest
import kite1412.gatetik.network.backend.dto.request.BackendCctvUpdateRequest
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.backend.extension.toCreateRequest
import kite1412.gatetik.network.backend.extension.toUpdateRequest
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Success

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

    override suspend fun addCctv(data: CctvCreate): CctvResult<Cctv> {
        val res = BackendClient
            .post<BackendCctvCreateRequest, BackendResponse<BackendCctv>>(
                path = "cctv",
                body = data.toCreateRequest()
            )

        if (res.errors != null) {
            val errors = res.errors

            when {
                errors.contains("path") -> return Error(CctvRepository.CctvError.PathIsAlreadyExist())
            }
        }

        return res.data?.toModel()?.let(::Success)
            ?: Error(CctvRepository.CctvError.BadRequest("Gagal menambah CCTV, harap coba lagi"))
    }

    override suspend fun updateCctv(data: CctvUpdate): CctvResult<Cctv> {
        val res = BackendClient
            .patch<BackendCctvUpdateRequest, BackendResponse<BackendCctv>>(
                path = "cctv/${data.id}",
                body = data.toUpdateRequest()
            )

        if (res.errors != null) {
            val errors = res.errors

            when {
                errors.contains("path") -> return Error(CctvRepository.CctvError.PathIsAlreadyExist())
            }
        }

        return res.data?.toModel()?.let(::Success)
            ?: Error(CctvRepository.CctvError.BadRequest("Gagal menambah CCTV, harap coba lagi"))
    }

    override suspend fun deleteCctv(id: Int): Boolean = BackendClient
        .delete<BackendResponse<Unit>>("cctv/$id")
        .success
}