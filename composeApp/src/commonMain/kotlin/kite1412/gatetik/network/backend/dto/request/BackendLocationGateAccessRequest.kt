package kite1412.gatetik.network.backend.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendLocationGateAccessRequest(
    @SerialName("gate_id")
    val gateId: Int,
    @SerialName("access_method")
    val accessMethod: String,
    val latitude: Double,
    val longitude: Double,
    val notes: String? = null
)
