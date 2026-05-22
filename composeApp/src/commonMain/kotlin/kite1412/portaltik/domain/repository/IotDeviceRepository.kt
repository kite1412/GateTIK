package kite1412.portaltik.domain.repository

import kite1412.portaltik.model.IotDevice
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result

typealias IotDeviceResult<T> = Result<T, Error>

interface IotDeviceRepository {
    suspend fun getByGateId(gateId: Int): IotDeviceResult<IotDevice?>
}