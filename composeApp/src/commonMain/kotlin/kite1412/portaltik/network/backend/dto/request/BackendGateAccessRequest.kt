package kite1412.portaltik.network.backend.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendGateAccessRequest(
    @SerialName("gate_id")
    val gateId: Int,
    @SerialName("access_method")
    val accessMethod: String,
    val notes: String? = null
)
