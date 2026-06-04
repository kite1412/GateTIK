package kite1412.gatetik.domain.repository

import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kotlinx.coroutines.flow.Flow

typealias ParkingQuotaResult<T> = Result<T, Error>

interface ParkingQuotaRepository {
    fun observeMainParkingQuota(): Flow<ParkingQuotaResult<ParkingQuota?>>
}