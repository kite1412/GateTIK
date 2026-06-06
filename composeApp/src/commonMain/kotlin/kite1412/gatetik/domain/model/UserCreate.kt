package kite1412.gatetik.domain.model

import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kotlin.time.Instant

data class UserCreate(
    val fullName: String,
    val email: String,
    val password: String,
    val institutionNumber: String,
    val phoneNumber: String?,
    val role: UserRole,
    val status: UserStatus,
    val profilePhoto: String?,
    val ktmPath: String?,
    val lastLoginAt: Instant?
)
