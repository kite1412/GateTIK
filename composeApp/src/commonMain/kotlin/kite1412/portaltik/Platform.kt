package kite1412.portaltik

enum class PlatformType {
    MOBILE, DESKTOP
}

data class Platform(
    val type: PlatformType,
    val name: String
)

expect fun getPlatform(): Platform