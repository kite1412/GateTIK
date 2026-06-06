package kite1412.gatetik.domain.model

import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus

data class UserUpdate(
    val id: Int,
    val fullName: String,
    val password: String? = null,
    val npmNip: String,
    val phoneNumber: String? = null,
    val role: UserRole,
    val status: UserStatus,
    val ktmPath: String? = null
)
