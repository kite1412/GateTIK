package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.network.backend.dto.request.BackendParkingQuotaUpdateRequest

fun ParkingQuota.toUpdateRequest() = BackendParkingQuotaUpdateRequest(
    totalSlots = totalSlots,
    autoRestrictStudent = autoRestrictStudents
)