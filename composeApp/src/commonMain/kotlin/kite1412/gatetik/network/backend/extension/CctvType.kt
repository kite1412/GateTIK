package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.model.CctvType
import kite1412.gatetik.network.backend.dto.model.BackendCctvType

fun CctvType.toModel() = when (this) {
    CctvType.MONITOR -> BackendCctvType.MONITOR
    CctvType.INTERCOM -> BackendCctvType.INTERCOM
}