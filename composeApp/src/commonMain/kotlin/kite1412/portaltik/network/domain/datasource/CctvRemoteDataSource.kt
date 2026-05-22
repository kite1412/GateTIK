package kite1412.portaltik.network.domain.datasource

import kite1412.portaltik.model.Cctv

interface CctvRemoteDataSource {
    suspend fun getCctvs(): List<Cctv>

    suspend fun getMainCctv(): Cctv?
}