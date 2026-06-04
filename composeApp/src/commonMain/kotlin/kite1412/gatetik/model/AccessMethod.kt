package kite1412.gatetik.model

enum class AccessMethod {
    MOBILE, WEB, DESKTOP;

    val capitalizedName = this.name.lowercase().replaceFirstChar { it.uppercase() }
}