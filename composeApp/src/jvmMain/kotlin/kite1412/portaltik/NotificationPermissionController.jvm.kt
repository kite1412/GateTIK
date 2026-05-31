package kite1412.portaltik

actual object NotificationPermissionController {
    actual fun isPermissionGranted(): Boolean = true

    actual fun getPermissionString(): String = ""
}