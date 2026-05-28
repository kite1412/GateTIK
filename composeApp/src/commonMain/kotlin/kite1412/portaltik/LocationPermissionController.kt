package kite1412.portaltik

interface LocationPermissionController {
    fun isPermissionGranted(): Boolean
    fun requestPermission(): PermissionResult
}

enum class PermissionResult {
    GRANTED, DENIED, PERMANENTLY_DENIED
}

