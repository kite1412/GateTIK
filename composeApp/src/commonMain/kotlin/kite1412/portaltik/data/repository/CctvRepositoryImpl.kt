package kite1412.portaltik.data.repository

import kite1412.portaltik.data.util.tryOrThrowUnknown
import kite1412.portaltik.domain.repository.CctvRepository
import kite1412.portaltik.domain.repository.CctvResult
import kite1412.portaltik.model.Cctv
import kite1412.portaltik.network.domain.datasource.CctvRemoteDataSource

class CctvRepositoryImpl(
    private val remoteDataSource: CctvRemoteDataSource
) : CctvRepository {
    private val logTag = "CctvRepositoryImpl"

    override suspend fun getCctvs(): CctvResult<List<Cctv>> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get cctvs",
        action = remoteDataSource::getCctvs
    )

    override suspend fun getMainCctv(): CctvResult<Cctv?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main cctv",
        action = remoteDataSource::getMainCctv
    )
}