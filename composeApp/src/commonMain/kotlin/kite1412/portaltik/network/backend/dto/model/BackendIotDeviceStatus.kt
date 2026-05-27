package kite1412.portaltik.network.backend.dto.model

import kite1412.portaltik.model.IotDeviceStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendIotDeviceStatus {
    @SerialName("online")
    ONLINE,
    @SerialName("offline")
    OFFLINE;

    fun toModel() = when (this) {
        ONLINE -> IotDeviceStatus.ONLINE
        OFFLINE -> IotDeviceStatus.OFFLINE
    }
}
