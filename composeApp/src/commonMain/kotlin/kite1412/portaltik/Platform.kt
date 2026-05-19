package kite1412.portaltik

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform