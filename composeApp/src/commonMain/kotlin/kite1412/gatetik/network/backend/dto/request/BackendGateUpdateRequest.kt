package kite1412.gatetik.network.backend.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendGateUpdateRequest(
    @SerialName("gate_name")
    val gateName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("allowed_radius_meter")
    val allowedRadiusMeter: Int? = null
)
