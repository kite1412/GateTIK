package kite1412.portaltik.network.backend.dto.model

import kotlinx.serialization.Serializable

@Serializable
data class BackendResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T?
)
