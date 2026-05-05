package kite1412.parking.gatecontrol

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform