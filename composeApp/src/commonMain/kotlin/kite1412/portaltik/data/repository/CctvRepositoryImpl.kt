package kite1412.portaltik.data.repository

import kite1412.portaltik.Logger
import kite1412.portaltik.domain.repository.CctvRepository
import kite1412.portaltik.domain.repository.CctvResult
import kite1412.portaltik.model.Cctv
import kite1412.portaltik.network.domain.datasource.CctvRemoteDataSource
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Success
import kite1412.portaltik.util.Unknown

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