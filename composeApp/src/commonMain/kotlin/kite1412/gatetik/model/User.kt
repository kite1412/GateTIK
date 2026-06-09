package kite1412.gatetik.model

import kotlin.time.Instant

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val role: UserRole,
    val status: UserStatus,
    val institutionNumber: String,
    val phoneNumber: String? = null,
    val ktmPath: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
    val lastLoginAt: Instant? = null
)
