package kite1412.gatetik.network.backend.dto.request

import kite1412.gatetik.network.backend.dto.model.BackendUserRole
import kite1412.gatetik.network.backend.dto.model.BackendUserStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendUserCreateRequest(
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    @SerialName("npm_nip")
    val npmNip: String,
    @SerialName("phone_number")
    val phoneNumber: String?,
    val role: BackendUserRole,
    val status: BackendUserStatus,
    @SerialName("profile_photo")
    val profilePhoto: String?,
    @SerialName("ktm_path")
    val ktmPath: String?,
    @SerialName("last_login_at")
    val lastLoginAt: Instant?
)
