package kite1412.portaltik.datastore.model

import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.model.UserStatus
import kotlinx.serialization.Serializable

@Serializable
data class DataStoreUser(
    val id: Int,
    val fullName: String,
    val email: String,
    val role: String,
    val status: String,
    val instituteNumber: String
) {
    fun toModel() = User(
        id = id,
        fullName = fullName,
        email = email,
        role = UserRole.valueOf(role),
        status = UserStatus.valueOf(status),
        instituteNumber = instituteNumber
    )
}
