package kite1412.portaltik.network.backend.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class BackendResponse<T>(
    val success: Boolean = false,
    val message: String? = null,
    val data: T? = null,
    val pagination: BackendPagination? = null,
    val errors: Map<String, List<String>>? = null
)