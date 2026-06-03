package kite1412.gatetik

expect object NotificationPermissionController {
    fun isPermissionGranted(): Boolean
    fun getPermissionString(): String
}