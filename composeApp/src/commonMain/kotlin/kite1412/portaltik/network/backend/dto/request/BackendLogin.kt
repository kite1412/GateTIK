package kite1412.portaltik.network.backend.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class BackendLogin(
    val email: String,
    val password: String
)
