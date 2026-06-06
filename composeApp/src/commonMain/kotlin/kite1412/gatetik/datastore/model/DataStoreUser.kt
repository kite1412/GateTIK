package kite1412.gatetik.datastore.model

import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class DataStoreUser(
    val id: Int,
    val fullName: String,
    val email: String,
    val role: String,
    val status: String,
    val instituteNumber: String,
    val createdAt: Instant
) {
    fun toModel() = User(
        id = id,
        fullName = fullName,
        email = email,
        role = UserRole.valueOf(role),
        status = UserStatus.valueOf(status),
        institutionNumber = instituteNumber,
        createdAt = createdAt
    )
}
