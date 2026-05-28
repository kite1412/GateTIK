package kite1412.portaltik

actual fun getPlatform(): Platform = Platform(
    type = PlatformType.MOBILE,
    name = "Android"
)