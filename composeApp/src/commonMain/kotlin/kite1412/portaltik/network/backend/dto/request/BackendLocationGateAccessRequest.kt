package kite1412.portaltik.network.backend.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class BackendLocationGateAccessRequest(
    val gateId: Int,
    val accessMethod: String,
    val latitude: Double,
    val longitude: Double,
    val notes: String? = null
)
