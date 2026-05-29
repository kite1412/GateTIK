package kite1412.portaltik

actual object LocationPermissionController {
    actual fun isPermissionGranted(): Boolean = false

    actual fun getPermissionString(): String = ""
}