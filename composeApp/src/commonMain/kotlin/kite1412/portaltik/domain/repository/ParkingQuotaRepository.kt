package kite1412.portaltik.domain.repository

import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result

typealias ParkingQuotaResult<T> = Result<T, Error>

interface ParkingQuotaRepository {
    suspend fun getMainParkingQuota(): ParkingQuotaResult<ParkingQuota?>
}