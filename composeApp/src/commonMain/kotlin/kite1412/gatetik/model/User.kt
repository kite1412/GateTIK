package kite1412.gatetik.model

// TODO change later
data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val role: UserRole,
    val status: UserStatus,
    val instituteNumber: String
)
