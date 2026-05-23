package kite1412.portaltik.feature.admin.mobile.gate

import androidx.lifecycle.ViewModel
import kite1412.portaltik.domain.repository.AccessLogRepository
import kite1412.portaltik.domain.repository.GateRepository
import kite1412.portaltik.domain.repository.IotDeviceRepository

class MobileAdminGateViewModel(
    private val gateRepository: GateRepository,
    private val iotDeviceRepository: IotDeviceRepository,
    private val accessLogRepository: AccessLogRepository
) : ViewModel() {

}