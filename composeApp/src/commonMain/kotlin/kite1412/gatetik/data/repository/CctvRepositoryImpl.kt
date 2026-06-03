package kite1412.gatetik.data.repository

import kite1412.gatetik.Logger
import kite1412.gatetik.domain.repository.CctvRepository
import kite1412.gatetik.domain.repository.CctvResult
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Success
import kite1412.gatetik.util.Unknown

class CctvRepositoryImpl(
    private val remoteDataSource: CctvRemoteDataSource
) : CctvRepository {
    private val logTag = "CctvRepositoryImpl"

    override suspend fun getMainCctv(): CctvResult<Cctv?> = try {
        val cctv = remoteDataSource.getMainCctv()

        Success(cctv)
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to get main cctv",
            throwable = e
        )
        Error(Unknown())
    }
}