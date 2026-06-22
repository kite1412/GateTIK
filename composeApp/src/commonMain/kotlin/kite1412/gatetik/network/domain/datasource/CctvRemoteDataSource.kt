package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.model.Cctv

interface CctvRemoteDataSource {
    suspend fun getMainCctv(): Cctv?

    suspend fun getAll(): List<Cctv>

    suspend fun addCctv(data: CctvCreate): Cctv

    suspend fun updateCctv(data: CctvUpdate): Cctv

    suspend fun deleteCctv(id: Int): Boolean
}