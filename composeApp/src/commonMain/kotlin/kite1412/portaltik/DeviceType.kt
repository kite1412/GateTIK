package kite1412.portaltik

enum class DeviceType {
    MOBILE, DESKTOP
}

expect fun getDeviceType(): DeviceType