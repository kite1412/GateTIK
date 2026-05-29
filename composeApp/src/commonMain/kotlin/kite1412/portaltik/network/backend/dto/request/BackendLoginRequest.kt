package kite1412.portaltik.network.backend.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class BackendLoginRequest(
    val email: String,
    val password: String
)
