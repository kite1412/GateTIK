package kite1412.gatetik.network.backend.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendProfileUpdateRequest(
    val id: Int,
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    @SerialName("npm_nip")
    val npmNip: String,
    @SerialName("phone_number")
    val phoneNumber: String?
)
