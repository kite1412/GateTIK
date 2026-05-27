package kite1412.portaltik.network.backend.dto.model

import kite1412.portaltik.model.IotDevice
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendIotDevice(
    val id: Int,
    @SerialName("device_name")
    val deviceName: String,
    @SerialName("device_uid")
    val deviceUid: String,
    @SerialName("gate_id")
    val gateId: Int,
    @SerialName("firmware_version")
    val firmwareVersion: String,
    val status: BackendIotDeviceStatus,
    @SerialName("last_online_at")
    val lastOnlineAt: Instant,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
) {
    fun toModel() = IotDevice(
        id = id,
        deviceName = deviceName,
        deviceUid = deviceUid,
        gateId = gateId,
        firmwareVersion = firmwareVersion,
        status = status.toModel(),
        lastOnlineAt = lastOnlineAt
    )
}
