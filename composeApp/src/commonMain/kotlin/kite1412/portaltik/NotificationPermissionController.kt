package kite1412.portaltik

expect object NotificationPermissionController {
    fun isPermissionGranted(): Boolean
    fun getPermissionString(): String
}