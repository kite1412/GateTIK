package kite1412.gatetik.domain.repository

import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result

typealias CctvResult<T> = Result<T, Error>

interface CctvRepository {
    suspend fun getMainCctv(): CctvResult<Cctv?>

    suspend fun getAll(): CctvResult<List<Cctv>>

    suspend fun addCctv(data: CctvCreate): CctvResult<Cctv>

    suspend fun updateCctv(data: CctvUpdate): CctvResult<Cctv>

    suspend fun deleteCctv(id: Int): CctvResult<Boolean>

    sealed interface CctvError : Error {
        data class PathIsAlreadyExist(
            override val message: String = "Path sudah digunakan, coba dengan path lain"
        ) : CctvError

        data class BadRequest(override val message: String) : CctvError

    }
}