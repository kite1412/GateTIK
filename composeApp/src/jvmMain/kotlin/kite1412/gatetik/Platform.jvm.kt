package kite1412.gatetik

import kite1412.gatetik.util.getSupportedOS

actual fun getPlatform(): Platform = Platform(
    type = PlatformType.DESKTOP,
    name = getSupportedOS().osName
)