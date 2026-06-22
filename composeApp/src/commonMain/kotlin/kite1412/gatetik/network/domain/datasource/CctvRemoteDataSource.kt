package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.domain.repository.CctvResult
import kite1412.gatetik.model.Cctv

interface CctvRemoteDataSource {
    suspend fun getMainCctv(): Cctv?

    suspend fun getAll(): List<Cctv>

    suspend fun addCctv(data: CctvCreate): CctvResult<Cctv>

    suspend fun updateCctv(data: CctvUpdate): CctvResult<Cctv>

    suspend fun deleteCctv(id: Int): Boolean
}