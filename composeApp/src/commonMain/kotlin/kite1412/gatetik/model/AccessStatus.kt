package kite1412.gatetik.model

enum class AccessStatus {
    SUCCESS, PENDING, FAILED;

    val capitalizedName = this.name.lowercase().replaceFirstChar { it.uppercase() }
}