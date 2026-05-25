package kite1412.portaltik.network.backend.dto.response

import kite1412.portaltik.network.backend.dto.model.BackendUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendLoginResponse(
    val token: String,
    @SerialName("token_type")
    val tokenType: String,
    val user: BackendUser
)
