package kite1412.gatetik.network.backend.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class BackendSignInRequest(
    val email: String,
    val password: String
)
