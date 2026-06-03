package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.repository.CctvRepository
import kite1412.gatetik.domain.repository.CctvResult
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.network.domain.datasource.CctvRemoteDataSource

class CctvRepositoryImpl(
    private val remoteDataSource: CctvRemoteDataSource
) : CctvRepository {
    private val logTag = "CctvRepositoryImpl"

    override suspend fun getMainCctv(): CctvResult<Cctv?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main cctv",
        action = remoteDataSource::getMainCctv
    )
}