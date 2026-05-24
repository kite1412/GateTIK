package kite1412.portaltik.model

enum class UserRole {
    ADMIN, STAFF, STUDENT;

    fun toIdString() = when (this) {
        ADMIN -> "Admin"
        STAFF -> "Staff"
        STUDENT -> "Mahasiswa"
    }
}