package kite1412.gatetik.network.backend.dto.model

import kite1412.gatetik.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class BackendUser(
    val id: Int,
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    @SerialName("npm_nip")
    val npmNip: String,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    val role: BackendUserRole,
    val status: BackendUserStatus,
    @SerialName("profile_photo")
    val profilePhoto: String? = null,
    @SerialName("ktm_path")
    val ktmPath: String? = null,
    @SerialName("last_login_at")
    val lastLoginAt: Instant? = null,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
) {
    fun toModel(): User = User(
        id = id,
        fullName = fullName,
        email = email,
        role = role.toModel(),
        status = status.toModel(),
        instituteNumber = npmNip,
        createdAt = createdAt
    )
}