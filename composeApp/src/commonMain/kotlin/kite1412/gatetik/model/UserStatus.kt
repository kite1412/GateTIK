package kite1412.gatetik.model

enum class UserStatus {
    PENDING, ACTIVE, SUSPENDED;

    val capitalizedName = name.lowercase().replaceFirstChar { it.uppercase() }
}