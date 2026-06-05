package kite1412.gatetik.model

import kotlin.time.Instant

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val role: UserRole,
    val status: UserStatus,
    val instituteNumber: String,
    val createdAt: Instant
)
