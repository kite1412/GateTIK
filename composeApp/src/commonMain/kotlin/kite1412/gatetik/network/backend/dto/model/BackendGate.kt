package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.Gate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendGate(
    val id: Int,
    @SerialName("gate_name")
    val gateName: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("allowed_radius_meter")
    val allowedRadiusMeter: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
    @SerialName("iot_devices")
    val iotDevices: List<BackendIotDevice>
) {
    fun toModel() = Gate(
        id = id,
        gateName = gateName,
        latitude = latitude,
        longitude = longitude,
        allowedRadiusMeter = allowedRadiusMeter,
        iotDevice = iotDevices.first().toModel()
    )
}
