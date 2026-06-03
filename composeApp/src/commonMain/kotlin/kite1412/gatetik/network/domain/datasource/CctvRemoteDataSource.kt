package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.model.Cctv

interface CctvRemoteDataSource {
    suspend fun getMainCctv(): Cctv?
}