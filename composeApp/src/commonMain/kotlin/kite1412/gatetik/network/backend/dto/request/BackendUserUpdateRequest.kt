package kite1412.gatetik.network.backend.dto.request

import kite1412.gatetik.network.backend.dto.model.BackendUserRole
import kite1412.gatetik.network.backend.dto.model.BackendUserStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendUserUpdateRequest(
    @SerialName("full_name")
    val fullName: String,
    val password: String? = null,
    @SerialName("npm_nip")
    val npmNip: String,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    val role: BackendUserRole,
    val status: BackendUserStatus,
    @SerialName("ktm_path")
    val ktmPath: String? = null
)
