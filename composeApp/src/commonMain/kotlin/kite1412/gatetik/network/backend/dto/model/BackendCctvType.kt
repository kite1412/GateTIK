package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.CctvType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendCctvType {
    @SerialName("monitor")
    MONITOR,
    @SerialName("intercom")
    INTERCOM;

    fun toModel() = when (this) {
        MONITOR -> CctvType.MONITOR
        INTERCOM -> CctvType.INTERCOM
    }
}