package kite1412.portaltik.network.backend.dto.model

import kite1412.portaltik.model.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackendUserRole {
    @SerialName("admin")
    ADMIN,
    @SerialName("staff")
    STAFF,
    @SerialName("mahasiswa")
    STUDENT;

    fun toModel(): UserRole = UserRole.valueOf(this.name)
}