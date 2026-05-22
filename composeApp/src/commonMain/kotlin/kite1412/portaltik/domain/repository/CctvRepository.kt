package kite1412.portaltik.domain.repository

import kite1412.portaltik.model.Cctv
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result

typealias CctvResult<T> = Result<T, Error>

interface CctvRepository {
    suspend fun getCctvs(): CctvResult<List<Cctv>>

    suspend fun getMainCctv(): CctvResult<Cctv?>
}