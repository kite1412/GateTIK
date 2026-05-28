package kite1412.portaltik

import kite1412.portaltik.util.getSupportedOS

actual fun getPlatform(): Platform = Platform(
    type = PlatformType.DESKTOP,
    name = getSupportedOS().osName
)