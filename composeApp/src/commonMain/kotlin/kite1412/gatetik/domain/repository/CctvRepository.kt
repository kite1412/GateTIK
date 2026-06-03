package kite1412.gatetik.domain.repository

import kite1412.gatetik.model.Cctv
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias CctvResult<T> = Result<T, Error>

interface CctvRepository {
    suspend fun getMainCctv(): CctvResult<Cctv?>
}