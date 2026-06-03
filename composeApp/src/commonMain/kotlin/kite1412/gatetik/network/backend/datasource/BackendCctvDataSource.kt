package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.model.Cctv
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendCctv
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource

class BackendCctvDataSource : CctvRemoteDataSource {
    override suspend fun getMainCctv(): Cctv? = BackendClient
        .get<BackendResponse<BackendCctv>>("cctv/main")
        .data
        ?.toModel()
}