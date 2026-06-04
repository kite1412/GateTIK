package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.model.Gate
import kite1412.gatetik.network.backend.dto.request.BackendGateUpdateRequest

fun Gate.toUpdateRequest() = BackendGateUpdateRequest(
    gateName = gateName,
    latitude = latitude,
    longitude = longitude,
    allowedRadiusMeter = allowedRadiusMeter
)